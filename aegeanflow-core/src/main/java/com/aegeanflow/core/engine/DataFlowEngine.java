package com.aegeanflow.core.engine;

import com.aegeanflow.core.node.NodeInfo;
import com.aegeanflow.core.NodeStatus;
import com.aegeanflow.core.exception.NoSuchNodeException;
import com.aegeanflow.core.exception.NodeRuntimeException;
import com.aegeanflow.core.flow.Flow;
import com.aegeanflow.core.flow.FlowConnection;
import com.aegeanflow.core.flow.FlowNode;
import com.aegeanflow.core.node.CompilerUtil;
import com.aegeanflow.core.node.NodeRepository;
import com.aegeanflow.core.spi.Node;
import com.aegeanflow.core.spi.RunnableNode;
import com.aegeanflow.core.spi.annotation.NodeOutput;
import com.aegeanflow.core.spi.annotation.NodeOutputBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nullable;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.concurrent.*;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import static java.lang.String.format;

/**
 * Created by gorkem on 12.01.2018.
 */
public class DataFlowEngine {

    private static final Logger LOGGER = LoggerFactory.getLogger(DataFlowEngine.class);

    private final Flow flow;

    private final List<RunnableNode<?>> runnableNodeList;

    private final Map<UUID, FlowFuture> outputState;

    private final Map<UUID, FlowFuture> runningTasks;

    private Map<UUID, NodeStatus> statusMap;

    private final ExecutorService executor = Executors.newFixedThreadPool(4);

    private final NodeRepository nodeRepository;

    public DataFlowEngine(Flow flow, List<RunnableNode<?>> runnableNodeList, NodeRepository nodeRepository, @Nullable DataFlowEngine stateProvider) {
        this.flow = flow;
        this.runnableNodeList = runnableNodeList;
        this.nodeRepository = nodeRepository;
        if (stateProvider != null) {
            this.outputState = stateProvider.outputState;
        }else{
            this.outputState = new Hashtable<>();
        }
        this.runningTasks = new Hashtable<>();
        this.statusMap = new Hashtable<>();
    }

    private void updateStatus(Node node, NodeStatus status){
        statusMap.put(node.getUUID(), status);
        LOGGER.info(format("%s, %s, %s : %s", node.getName(), node.getNodeClass().getSimpleName(), node.getUUID(), status));
    }

    public List<Object> getResultList()  throws NoSuchNodeException, NodeRuntimeException {
        List<FlowFuture> futureList = getResultFutureList();
        List<Object> resultList = new ArrayList<>();
        for(FlowFuture future : futureList) {
            try {
                boolean done = future.isDone();
                Object result = future.get();
                if (!done) {
                    updateStatus(future.getNode(), NodeStatus.DONE);
                }
                resultList.add(result);
            } catch (InterruptedException e) {
                updateStatus(future.getNode(), NodeStatus.CANCELED);
            } catch (ExecutionException e) {
                updateStatus(future.getNode(), NodeStatus.FAILED);
                throw new NodeRuntimeException(e.getCause().getCause(), future.getUuid());
            }
        }
        return  resultList;
    }

    public List<FlowFuture> getResultFutureList() throws NoSuchNodeException, NodeRuntimeException {
        List<FlowNode> outputNodes = flow.getNodeList().stream()
                .filter(node -> flow.getConnectionList().stream()
                        .filter(conn -> conn.getType() == FlowConnection.Type.FLOW && conn.getFromUUID().equals(node.getUUID())).count() == 0)
                .collect(Collectors.toList());
        List<FlowFuture> flowFutureList = new ArrayList<>();
        for (FlowNode node : outputNodes) {
            updateStatus(node, NodeStatus.WAITING);
            flowFutureList.add(getResult(node.getUUID()));
        }
        return flowFutureList;
    }

    public FlowFuture getResult(UUID nodeUUID) throws NoSuchNodeException, NodeRuntimeException {
        for (FlowNode flowNode : flow.getNodeList()){
            setNodeConfig(getNode(flowNode.getUUID()), flowNode.getConfiguration());
        }
        RunnableNode<?> runnableNode = getNode(nodeUUID);
        return runNode(runnableNode, true);
    }

    private <T> FlowFuture<T> runNode(RunnableNode<T> runnableNode, boolean useState) throws NodeRuntimeException {
        try {
            //CONTROL STATE FROM PREVIOUS RUN
            if (useState && outputState.containsKey(runnableNode.getUUID())){
                FlowFuture flowFuture = outputState.get(runnableNode.getUUID());
                if (flowFuture.isDone() && !flowFuture.isCompletedExceptionally()){
                    return flowFuture;
                }
            }
            //CONTROL IF ALREADY RUNNING
            FlowFuture<T> flowFuture;
            if ((flowFuture = runningTasks.get(runnableNode.getUUID())) != null) {
                return flowFuture;
            }
            //RUN NODE
            Supplier<T> futureTask = () -> {
                //RUN INPUT NODES
                try {
                    List<IOPair> IOPairList = getIOPairList(runnableNode.getUUID());
                    List<OutputFuture> outputFutures = new ArrayList<>();
                    for (IOPair ioPair : IOPairList) {
                        FlowFuture outputFuture = runNode(ioPair.runnableNode, useState);
                        outputFutures.add(new OutputFuture(ioPair, outputFuture));
                    }

                    List<FlowInput> inputList = new ArrayList<>();
                    for (OutputFuture outputFuture : outputFutures) {
                        try {
                            Object input = outputFuture.get();
                            if (outputFuture.getIoPair().type == FlowConnection.Type.FLOW) {
                                if (input.getClass().getAnnotation(NodeOutputBean.class) != null) {
                                    for (Method method : input.getClass().getMethods()) {
                                        NodeOutput nodeOutput = method.getAnnotation(NodeOutput.class);
                                        if (nodeOutput != null && CompilerUtil.getVarName(method).equals(outputFuture.getIoPair().outputName)) {
                                            inputList.add(new FlowInput(outputFuture.getIoPair().inputName, method.invoke(input)));
                                        }
                                    }
                                } else {
                                    inputList.add(new FlowInput(outputFuture.getIoPair().inputName, input));
                                }
                            }
                            updateStatus(outputFuture.getIoPair().runnableNode, NodeStatus.DONE);
                        } catch (ExecutionException e) {
                            updateStatus(outputFuture.getIoPair().runnableNode, NodeStatus.FAILED);
                            if (e.getCause() instanceof NodeRuntimeException) {
                                throw new NodeRuntimeException(e.getCause().getCause(), runnableNode.getUUID());
                            }
                            throw new NodeRuntimeException(e.getCause(), runnableNode.getUUID());
                        }
                    }
                    setNodeInputs(runnableNode, inputList);
                    updateStatus(runnableNode, NodeStatus.RUNNING);
                    return runnableNode.call();
                }catch (NodeRuntimeException e) {
                    updateStatus(runnableNode, NodeStatus.FAILED);
                    throw e;
                }catch (Exception e) {
                    updateStatus(runnableNode, NodeStatus.FAILED);
                    throw new NodeRuntimeException(e, runnableNode.getUUID());
                }
            };
            CompletableFuture<T> completableFuture = CompletableFuture.supplyAsync(futureTask, executor);
            flowFuture = new FlowFuture<>(runnableNode, completableFuture);
            runningTasks.put(runnableNode.getUUID(), flowFuture);
            outputState.put(runnableNode.getUUID(), flowFuture);
            return flowFuture;
        } catch (Exception e) {
            //SET STATUS AS CANCELLED for WAITING and RUNNING Nodes
            statusMap = statusMap.entrySet().stream().map(entry -> {
                if (entry.getValue().equals(NodeStatus.WAITING) || entry.getValue().equals(NodeStatus.RUNNING)) {
                    return new AbstractMap.SimpleEntry<>(entry.getKey(), NodeStatus.CANCELED);
                }
                return entry;
            }).collect(Collectors.toMap(entry -> entry.getKey(), entry -> entry.getValue()));
            //TODO: remove and trigger runnableNode status listerner
            StringJoiner sj = new StringJoiner("\n", "--------\n", "");
            statusMap.forEach((k, v) -> sj.add(k.toString() + " - " + v));
            System.out.println(sj.toString());
            throw new NodeRuntimeException(e, runnableNode.getUUID());
        }
    }

    private void setNodeInputs(RunnableNode runnableNode, List<FlowInput> inputs){
        try {
            Optional<NodeInfo> nodeInfoOptional = nodeRepository.getNodeInfoList().stream()
                    .filter(nodeInfo -> nodeInfo.getNodeClass().equals(runnableNode.getClass()))
                    .findFirst();
            if (nodeInfoOptional.isPresent()) {
                NodeInfo nodeInfo = nodeInfoOptional.get();
                for (FlowInput flowInput : inputs) {
                    Method method = nodeInfo.getInputMethods().get(flowInput.getName());
                    method.invoke(runnableNode, flowInput.getValue());
                }
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    private void setNodeConfig(RunnableNode runnableNode, Map<String, Object> configs){
        try {
            Optional<NodeInfo> compiledNodeInfoOptional = nodeRepository.getNodeInfoList().stream()
                    .filter(nodeInfo -> nodeInfo.getNodeClass().equals(runnableNode.getClass()))
                    .findFirst();
            if (compiledNodeInfoOptional.isPresent()) {
                NodeInfo nodeInfo = compiledNodeInfoOptional.get();
                for (Map.Entry<String, Object> config : configs.entrySet()) {
                    Method method = nodeInfo.getConfigMethods().get(config.getKey());
                    method.invoke(runnableNode, config.getValue());
                }
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    public static class IOPair {
        public final String outputName;

        public final String inputName;

        public final RunnableNode<?> runnableNode;

        public final FlowConnection.Type type;

        public IOPair(String outputName, String inputName, RunnableNode<?> runnableNode, FlowConnection.Type type) {
            this.outputName = outputName;
            this.inputName = inputName;
            this.runnableNode = runnableNode;
            this.type = type;
        }
    }

    public List<IOPair> getIOPairList(UUID nodeUUID) throws NoSuchNodeException {
        List<FlowConnection> inputConnections = flow.getConnectionList().stream()
                .filter(flowConnection -> flowConnection.getToUUID().equals(nodeUUID))
                .collect(Collectors.toList());
        List<IOPair> IOPairList = new ArrayList<>();
        for (FlowConnection inputConnection: inputConnections) {
            IOPair ioPair =
                    new IOPair(inputConnection.getOutputName(), inputConnection.getInputName(),
                            getNode(inputConnection.getFromUUID()), inputConnection.getType());
            IOPairList.add(ioPair);
        }
        return IOPairList;
    }

    private RunnableNode<?> getNode(UUID nodeUUID) throws NoSuchNodeException {
        return runnableNodeList.stream().filter(node -> node.getUUID().equals(nodeUUID))
                .findFirst()
                .orElseThrow(() -> new NoSuchNodeException(nodeUUID));
    }

    private FlowNode getFlowNode(UUID nodeUUID) throws NoSuchNodeException {
        return flow.getNodeList().stream().filter(node -> node.getUUID().equals(nodeUUID))
                .findFirst()
                .orElseThrow(() -> new NoSuchNodeException(nodeUUID));
    }
}
