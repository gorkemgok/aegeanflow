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
import com.aegeanflow.core.spi.annotation.NodeOutput;
import com.aegeanflow.core.spi.annotation.NodeOutputBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nullable;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.concurrent.*;
import java.util.stream.Collectors;

/**
 * Created by gorkem on 12.01.2018.
 */
public class DataFlowEngine {

    private static final Logger LOGGER = LoggerFactory.getLogger(DataFlowEngine.class);

    private final Flow flow;

    private final List<Node<?>> nodeList;

    private final Map<UUID, FlowFuture> outputState;

    private final Map<UUID, FlowFuture> runningTasks;

    private Map<UUID, NodeStatus> statusMap;

    private final ExecutorService executor = Executors.newFixedThreadPool(4);

    private final NodeRepository nodeRepository;

    public DataFlowEngine(Flow flow, List<Node<?>> nodeList, NodeRepository nodeRepository, @Nullable DataFlowEngine stateProvider) {
        this.flow = flow;
        this.nodeList = nodeList;
        this.nodeRepository = nodeRepository;
        if (stateProvider != null) {
            this.outputState = stateProvider.outputState;
        }else{
            this.outputState = new Hashtable<>();
        }
        this.runningTasks = new Hashtable<>();
        this.statusMap = new Hashtable<>();
    }

    private void updateStatus(UUID uuid, NodeStatus status){
        statusMap.put(uuid, status);
        //TODO: remove and trigger node status listerner
        StringJoiner sj = new StringJoiner("\n", "--------\n", "");
        statusMap.forEach((k, v) -> sj.add(k.toString() + " - " + v));
        System.out.println(sj.toString());
    }

    public List<FlowFuture> getResultList() throws NoSuchNodeException, NodeRuntimeException {
        List<FlowNode> outputNodes = flow.getNodeList().stream()
                .filter(node -> flow.getConnectionList().stream()
                        .filter(conn -> conn.getFromUUID().equals(node.getUUID())).count() == 0)
                .collect(Collectors.toList());
        List<FlowFuture> flowFutureList = new ArrayList<>();
        for (FlowNode node : outputNodes) {
            updateStatus(node.getUUID(), NodeStatus.WAITING);
            flowFutureList.add(getResult(node.getUUID()));
        }
        return flowFutureList;
    }

    public FlowFuture getResult(UUID nodeUUID) throws NoSuchNodeException, NodeRuntimeException {
        for (FlowNode flowNode : flow.getNodeList()){
            setNodeConfig(getNode(flowNode.getUUID()), flowNode.getConfiguration());
        }
        Node<?> node = getNode(nodeUUID);
        return runNode(node, true);
    }

    private <T> FlowFuture<T> runNode(Node<T> node, boolean useState) throws NodeRuntimeException {
        try {
            //CONTROL STATE FROM PREVIOUS RUN
            if (useState && outputState.containsKey(node.getUUID())){
                return outputState.get(node.getUUID());
            }
            //CONTROL IF ALREADY RUNNING
            FlowFuture<T> flowFuture;
            if ((flowFuture = runningTasks.get(node.getUUID())) != null) {
                return flowFuture;
            }
            //RUN NODE
            FutureTask<T> futureTask = new FutureTask<>(() -> {
                //RUN INPUT NODES
                List<IOPair> IOPairList = getIOPairList(node.getUUID());
                List<OutputFuture> outputFutures = new ArrayList<>();
                for (IOPair ioPair : IOPairList) {
                    FlowFuture outputFuture = runNode(ioPair.node, useState);
                    outputFutures.add(new OutputFuture(ioPair.outputName, ioPair.inputName, outputFuture));
                }

                List <FlowInput> inputList = new ArrayList<>();
                for (OutputFuture outputFuture : outputFutures){
                    try {
                        Object input = outputFuture.get();
                        if (input.getClass().getAnnotation(NodeOutputBean.class) != null) {
                            for (Method method : input.getClass().getMethods()) {
                                NodeOutput nodeOutput = method.getAnnotation(NodeOutput.class);
                                if (nodeOutput != null && CompilerUtil.getVarName(method).equals(outputFuture.getOutputName())) {
                                    inputList.add(new FlowInput(outputFuture.getInputName(), method.invoke(input)));
                                }
                            }
                        } else {
                          inputList.add(new FlowInput(outputFuture.getInputName(), input));
                        }
                        updateStatus(outputFuture.getUUID(), NodeStatus.DONE);
                    }catch (ExecutionException e){
                        updateStatus(outputFuture.getUUID(), NodeStatus.FAILED);
                        throw new NodeRuntimeException(e.getCause(), node.getUUID());
                    }
                }
                setNodeInputs(node, inputList);
                updateStatus(node.getUUID(), NodeStatus.RUNNING);
                LOGGER.info("Running node {}", node.getUUID());
                return node.call();
            });
            flowFuture = new FlowFuture<>(node.getUUID(), futureTask);
            runningTasks.put(node.getUUID(), flowFuture);
            outputState.put(node.getUUID(), flowFuture);
            executor.submit(futureTask);
            return flowFuture;
        } catch (Exception e) {
            //SET STATUS AS CANCELLED for WAITING and RUNNING Nodes
            statusMap = statusMap.entrySet().stream().map(entry -> {
                if (entry.getValue().equals(NodeStatus.WAITING) || entry.getValue().equals(NodeStatus.RUNNING)) {
                    return new AbstractMap.SimpleEntry<>(entry.getKey(), NodeStatus.CANCELED);
                }
                return entry;
            }).collect(Collectors.toMap(entry -> entry.getKey(), entry -> entry.getValue()));
            //TODO: remove and trigger node status listerner
            StringJoiner sj = new StringJoiner("\n", "--------\n", "");
            statusMap.forEach((k, v) -> sj.add(k.toString() + " - " + v));
            System.out.println(sj.toString());
            throw new NodeRuntimeException(e, node.getUUID());
        }
    }

    private void setNodeInputs(Node node, List<FlowInput> inputs){
        try {
            Optional<NodeInfo> nodeInfoOptional = nodeRepository.getNodeInfoList().stream()
                    .filter(nodeInfo -> nodeInfo.getNodeClass().equals(node.getClass()))
                    .findFirst();
            if (nodeInfoOptional.isPresent()) {
                NodeInfo nodeInfo = nodeInfoOptional.get();
                for (FlowInput flowInput : inputs) {
                    Method method = nodeInfo.getInputMethods().get(flowInput.getName());
                    method.invoke(node, flowInput.getValue());
                }
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    private void setNodeConfig(Node node, Map<String, Object> configs){
        try {
            Optional<NodeInfo> compiledNodeInfoOptional = nodeRepository.getNodeInfoList().stream()
                    .filter(nodeInfo -> nodeInfo.getNodeClass().equals(node.getClass()))
                    .findFirst();
            if (compiledNodeInfoOptional.isPresent()) {
                NodeInfo nodeInfo = compiledNodeInfoOptional.get();
                for (Map.Entry<String, Object> config : configs.entrySet()) {
                    Method method = nodeInfo.getConfigMethods().get(config.getKey());
                    method.invoke(node, config.getValue());
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

        public final Node<?> node;

        public IOPair(String outputName, String inputName, Node<?> node) {
            this.outputName = outputName;
            this.inputName = inputName;
            this.node = node;
        }
    }

//    private class InputUUIDPair {
//        public String inputName;
//
//        public UUID uuid;
//
//        public InputUUIDPair(String inputName, UUID uuid) {
//            this.inputName = inputName;
//            this.uuid = uuid;
//        }
//    }
    public List<IOPair> getIOPairList(UUID nodeUUID) throws NoSuchNodeException {
        List<FlowConnection> inputConnections = flow.getConnectionList().stream()
                .filter(flowConnection -> flowConnection.getToUUID().equals(nodeUUID))
                .collect(Collectors.toList());
        List<IOPair> IOPairList = new ArrayList<>();
        for (FlowConnection inputConnection: inputConnections) {
            IOPair ioPair =
                    new IOPair(inputConnection.getOutputName(), inputConnection.getInputName(),
                            getNode(inputConnection.getFromUUID()));
            IOPairList.add(ioPair);
        }
        return IOPairList;
//        List<InputUUIDPair> inputUUIDPairList = flow.getConnectionList().stream()
//                .filter(flowConnection -> flowConnection.getToUUID().equals(nodeUUID))
//                .map(flowConnection -> new InputUUIDPair(flowConnection.getInputName(), flowConnection.getFromUUID()))
//                .collect(Collectors.toList());
//        List<IOPair> IOPairList = new ArrayList<>();
//        for (InputUUIDPair inputUUIDPair : inputUUIDPairList){
//            Node<?> inputNode = getNode(inputUUIDPair.uuid);
//            IOPairList.add(new IOPair(outputName, inputUUIDPair.inputName, inputNode));
//        }
//        return IOPairList;
    }

    private Node<?> getNode(UUID nodeUUID) throws NoSuchNodeException {
        return nodeList.stream().filter(node -> node.getUUID().equals(nodeUUID))
                .findFirst()
                .orElseThrow(() -> new NoSuchNodeException(nodeUUID));
    }

    private FlowNode getFlowNode(UUID nodeUUID) throws NoSuchNodeException {
        return flow.getNodeList().stream().filter(node -> node.getUUID().equals(nodeUUID))
                .findFirst()
                .orElseThrow(() -> new NoSuchNodeException(nodeUUID));
    }
}
