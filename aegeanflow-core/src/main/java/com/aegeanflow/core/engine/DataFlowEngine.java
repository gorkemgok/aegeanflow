package com.aegeanflow.core.engine;

import com.aegeanflow.core.*;
import com.aegeanflow.core.definition.BoxConfigurationDefinition;
import com.aegeanflow.core.definition.BoxIODefinition;
import com.aegeanflow.core.exception.NoSuchNodeException;
import com.aegeanflow.core.exception.NodeRuntimeException;
import com.aegeanflow.core.proxy.SessionProxy;
import com.aegeanflow.core.proxy.RouteProxy;
import com.aegeanflow.core.proxy.NodeProxy;
import com.aegeanflow.core.spi.AnnotatedBox;
import com.aegeanflow.core.spi.annotation.NodeOutput;
import com.aegeanflow.core.spi.annotation.NodeOutputBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nullable;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.concurrent.*;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import static java.lang.String.format;

/**
 * Created by gorkem on 12.01.2018.
 */
public class DataFlowEngine {

    private static final Logger LOGGER = LoggerFactory.getLogger(DataFlowEngine.class);

    private final SessionProxy sessionProxy;

    private final List<AnnotatedBox<?>> annotatedBoxList;

    private final Map<UUID, FlowFuture> outputState;

    private final Map<UUID, FlowFuture> runningTasks;

    private Map<UUID, NodeStatus> statusMap;

    private final ExecutorService executor = Executors.newFixedThreadPool(4);

    private final BoxRepository boxRepository;

    public DataFlowEngine(SessionProxy sessionProxy, List<AnnotatedBox<?>> annotatedBoxList, BoxRepository boxRepository, @Nullable DataFlowEngine stateProvider) {
        this.sessionProxy = sessionProxy;
        this.annotatedBoxList = annotatedBoxList;
        this.boxRepository = boxRepository;
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
        if (status != NodeStatus.RUNNING) {
            runningTasks.remove(node.getUUID());
        }
        //LOGGER.info(format("%s, %s, %s : %s", box.getName(), box.getType().getSimpleName(), box.getUUID(), status));
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
        List<NodeProxy> outputNodes = sessionProxy.getNodeList().stream()
                .filter(node -> sessionProxy.getConnectionList().stream()
                        .filter(conn -> conn.getType() == RouteProxy.Type.FLOW && conn.getFromUUID().equals(node.getUUID())).count() == 0)
                .collect(Collectors.toList());
        List<FlowFuture> flowFutureList = new ArrayList<>();
        for (NodeProxy node : outputNodes) {
            //updateStatus(box, NodeStatus.WAITING);
            flowFutureList.add(getResult(node.getUUID()));
        }
        return flowFutureList;
    }

    public FlowFuture getResult(UUID nodeUUID) throws NoSuchNodeException, NodeRuntimeException {
        for (NodeProxy nodeProxy : sessionProxy.getNodeList()){
            setNodeConfig(getNode(nodeProxy.getUUID()), nodeProxy.getConfiguration());
        }
        AnnotatedBox<?> annotatedBox = getNode(nodeUUID);
        return startNode(annotatedBox, true);
    }

    private <T> FlowFuture<T> startNode(AnnotatedBox<T> annotatedBox, boolean useState) throws NodeRuntimeException {
        try {
            //CONTROL STATE FROM PREVIOUS RUN
            if (useState && outputState.containsKey(annotatedBox.getUUID())){
                FlowFuture flowFuture = outputState.get(annotatedBox.getUUID());
                if (flowFuture.isDone() && !flowFuture.isCompletedExceptionally()){
                    return flowFuture;
                }
            }
            //CONTROL IF ALREADY RUNNING
            FlowFuture<T> flowFuture;
            if ((flowFuture = runningTasks.get(annotatedBox.getUUID())) != null) {
                return flowFuture;
            }
            //RUN NODE
            Supplier<List<FlowInput>> supplyInputs = () -> {
                //RUN INPUT NODES
                try {
                    List<IOPair> IOPairList = getIOPairList(annotatedBox.getUUID());
                    List<OutputFuture> outputFutures = new ArrayList<>();
                    for (IOPair ioPair : IOPairList) {
                        FlowFuture outputFuture = startNode(ioPair.annotatedBox, useState);
                        outputFutures.add(new OutputFuture(ioPair, outputFuture));
                    }

                    List<FlowInput> inputList = new ArrayList<>();
                    for (OutputFuture outputFuture : outputFutures) {
                        try {
                            Object input = outputFuture.get();
                            if (outputFuture.getIoPair().type == RouteProxy.Type.FLOW) {
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
                            //updateStatus(outputFuture.getIoPair().annotatedBox, NodeStatus.DONE);
                        } catch (ExecutionException e) {
                            //updateStatus(outputFuture.getIoPair().annotatedBox, NodeStatus.FAILED);
                            if (e.getCause() instanceof NodeRuntimeException) {
                                throw new NodeRuntimeException(e.getCause().getCause(), annotatedBox.getUUID());
                            }
                            throw new NodeRuntimeException(e.getCause(), annotatedBox.getUUID());
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    return inputList;
                }catch (NodeRuntimeException e) {
                    //updateStatus(annotatedBox, NodeStatus.FAILED);
                    throw e;
                }catch (Exception e) {
                    //updateStatus(annotatedBox, NodeStatus.FAILED);
                    throw new NodeRuntimeException(e, annotatedBox.getUUID());
                }
            };

            Function<List<FlowInput>, T> nodeRunTask = (inputList) -> {
                //RUN INPUT NODES
                try {
                    setNodeInputs(annotatedBox, inputList);
                    //updateStatus(annotatedBox, NodeStatus.RUNNING);
                    return null;//annotatedBox.call();
                }catch (NodeRuntimeException e) {
                    //updateStatus(annotatedBox, NodeStatus.FAILED);
                    throw e;
                }catch (Exception e) {
                    //updateStatus(annotatedBox, NodeStatus.FAILED);
                    throw new NodeRuntimeException(e, annotatedBox.getUUID());
                }
            };
            CompletableFuture<T> completableFuture = CompletableFuture
                    .supplyAsync(supplyInputs)
                    .thenApply(nodeRunTask);
            //flowFuture = new FlowFuture<>(annotatedBox, completableFuture);
            runningTasks.put(annotatedBox.getUUID(), flowFuture);
            outputState.put(annotatedBox.getUUID(), flowFuture);
            return flowFuture;
        } catch (Exception e) {
            //SET STATUS AS CANCELLED for WAITING and RUNNING Nodes
            statusMap = statusMap.entrySet().stream().map(entry -> {
                if (entry.getValue().equals(NodeStatus.WAITING) || entry.getValue().equals(NodeStatus.RUNNING)) {
                    return new AbstractMap.SimpleEntry<>(entry.getKey(), NodeStatus.CANCELED);
                }
                return entry;
            }).collect(Collectors.toMap(entry -> entry.getKey(), entry -> entry.getValue()));
            //TODO: remove and trigger annotatedBox status listerner
            StringJoiner sj = new StringJoiner("\n", "--------\n", "");
            statusMap.forEach((k, v) -> sj.add(k.toString() + " - " + v));
            System.out.println(sj.toString());
            throw new NodeRuntimeException(e, annotatedBox.getUUID());
        }
    }

    private void setNodeInputs(AnnotatedBox annotatedBox, List<FlowInput> inputs){
        try {
            Optional<BoxInfo> nodeInfoOptional = boxRepository.getBoxInfoList().stream()
                    .filter(nodeInfo -> nodeInfo.getNodeClass().equals(annotatedBox.getClass()))
                    .findFirst();
            if (nodeInfoOptional.isPresent()) {
                BoxInfo boxInfo = nodeInfoOptional.get();
                for (FlowInput flowInput : inputs) {
                    Method method = boxInfo.getInputMethods().get(flowInput.getName());
                    method.invoke(annotatedBox, flowInput.getValue());
                }
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    private void setNodeConfig(AnnotatedBox annotatedBox, Map<String, Object> configs){
        try {
            Optional<BoxInfo> compiledNodeInfoOptional = boxRepository.getBoxInfoList().stream()
                    .filter(nodeInfo -> nodeInfo.getNodeClass().equals(annotatedBox.getClass()))
                    .findFirst();
            if (compiledNodeInfoOptional.isPresent()) {
                BoxInfo boxInfo = compiledNodeInfoOptional.get();
                for (Map.Entry<String, Object> config : configs.entrySet()) {
                    Optional<BoxIODefinition> configDefOptional =
                            boxInfo.getDefinition().getConfigurations().stream()
                                    .filter(configDef -> configDef.getName().equals(config.getKey())).findFirst();
                    if (configDefOptional.isPresent()) {
                        Method method = boxInfo.getConfigMethods().get(config.getKey());
                        Class<?> type = configDefOptional.get().getType();
                        Object value = config.getValue();
                        if (type.equals(Integer.class)) {
                            value = Integer.valueOf(value.toString());
                        }
                        method.invoke(annotatedBox, value);
                    }
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

        public final AnnotatedBox<?> annotatedBox;

        public final RouteProxy.Type type;

        public IOPair(String outputName, String inputName, AnnotatedBox<?> annotatedBox, RouteProxy.Type type) {
            this.outputName = outputName;
            this.inputName = inputName;
            this.annotatedBox = annotatedBox;
            this.type = type;
        }
    }

    public List<IOPair> getIOPairList(UUID nodeUUID) throws NoSuchNodeException {
        List<RouteProxy> inputConnections = sessionProxy.getConnectionList().stream()
                .filter(flowConnection -> flowConnection.getToUUID().equals(nodeUUID))
                .collect(Collectors.toList());
        List<IOPair> IOPairList = new ArrayList<>();
        for (RouteProxy inputConnection: inputConnections) {
            IOPair ioPair =
                    new IOPair(inputConnection.getOutputName(), inputConnection.getInputName(),
                            getNode(inputConnection.getFromUUID()), inputConnection.getType());
            IOPairList.add(ioPair);
        }
        return IOPairList;
    }

    private AnnotatedBox<?> getNode(UUID nodeUUID) throws NoSuchNodeException {
        return annotatedBoxList.stream().filter(node -> node.getUUID().equals(nodeUUID))
                .findFirst()
                .orElseThrow(() -> new NoSuchNodeException(nodeUUID));
    }

    private NodeProxy getFlowNode(UUID nodeUUID) throws NoSuchNodeException {
        return sessionProxy.getNodeList().stream().filter(node -> node.getUUID().equals(nodeUUID))
                .findFirst()
                .orElseThrow(() -> new NoSuchNodeException(nodeUUID));
    }
}
