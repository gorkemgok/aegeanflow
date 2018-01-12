package com.aegeanflow.core;

import com.aegeanflow.core.exception.NoSuchNodeException;
import com.aegeanflow.core.exception.NodeRuntimeException;
import com.aegeanflow.core.flow.Flow;
import com.aegeanflow.core.flow.FlowNode;
import com.sun.istack.internal.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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

    private final Map<UUID, Object> outputState;

    private final Map<UUID, FlowFuture> runningTasks;

    private final ExecutorService executor = Executors.newFixedThreadPool(4);

    private final List<CompiledNodeInfo> compiledNodeInfoList;

    public DataFlowEngine(Flow flow, List<Node<?>> nodeList, @Nullable DataFlowEngine stateProvider, List<CompiledNodeInfo> compiledNodeInfoList) {
        this.flow = flow;
        this.nodeList = nodeList;
        this.compiledNodeInfoList = compiledNodeInfoList;
        if (stateProvider != null) {
            this.outputState = stateProvider.outputState;
        }else{
            this.outputState = new Hashtable<>();
        }
        this.runningTasks = new Hashtable<>();
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
            if (useState && outputState.containsKey(node.getUUID())){
                return new FlowFuture<>(CompletableFuture.completedFuture((T) outputState.get(node.getUUID())));
            }
            List<InputNodePair> inputNodePairList = getInputNodes(node.getUUID());
            List<InputFuture> inputFutures = new ArrayList<>();
            for (InputNodePair inputNodePair : inputNodePairList) {
                Future future = runNode(inputNodePair.node, useState);
                inputFutures.add(new InputFuture(inputNodePair.inputName, future));
            }
            List<FlowInput> inputList = new ArrayList<>();
            for (InputFuture inputFuture : inputFutures){
                try {
                    Object input = inputFuture.get();
                    inputList.add(new FlowInput(inputFuture.getInputName(), input));
                }catch (ExecutionException e){
                    throw new NodeRuntimeException(e.getCause());
                }
            }
            setNodeInputs(node, inputList);
            LOGGER.info("Running node {}", node.getUUID());
            Future<T> future = executor.submit(node);
            return new FlowFuture<>(future);
        } catch (Exception e) {
            throw new NodeRuntimeException(e);
        }
    }

    private void setNodeInputs(Node node, List<FlowInput> inputs){
        try {
            Optional<CompiledNodeInfo> compiledNodeInfoOptional = compiledNodeInfoList.stream()
                    .filter(compiledNodeInfo -> compiledNodeInfo.getNodeClass().equals(node.getClass()))
                    .findFirst();
            if (compiledNodeInfoOptional.isPresent()) {
                CompiledNodeInfo compiledNodeInfo = compiledNodeInfoOptional.get();
                for (FlowInput flowInput : inputs) {
                    Method method = compiledNodeInfo.getInputMethods().get(flowInput.getName());
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
            Optional<CompiledNodeInfo> compiledNodeInfoOptional = compiledNodeInfoList.stream()
                    .filter(compiledNodeInfo -> compiledNodeInfo.getNodeClass().equals(node.getClass()))
                    .findFirst();
            if (compiledNodeInfoOptional.isPresent()) {
                CompiledNodeInfo compiledNodeInfo = compiledNodeInfoOptional.get();
                for (Map.Entry<String, Object> config : configs.entrySet()) {
                    Method method = compiledNodeInfo.getConfigMethods().get(config.getKey());
                    method.invoke(node, config.getValue());
                }
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    private class InputNodePair {
        public String inputName;

        public Node<?> node;

        public InputNodePair(String inputName, Node<?> node) {
            this.inputName = inputName;
            this.node = node;
        }
    }

    private class InputUUIDPair {
        public String inputName;

        public UUID uuid;

        public InputUUIDPair(String inputName, UUID uuid) {
            this.inputName = inputName;
            this.uuid = uuid;
        }
    }
    private List<InputNodePair> getInputNodes(UUID nodeUUID) throws NoSuchNodeException {
        List<InputUUIDPair> inputUUIDPairList = flow.getConnectionList().stream()
                .filter(flowConnection -> flowConnection.getToUUID().equals(nodeUUID))
                .map(flowConnection -> new InputUUIDPair(flowConnection.getToInput(), flowConnection.getFromUUID()))
                .collect(Collectors.toList());
        List<InputNodePair> inputNodePairList = new ArrayList<>();
        for (InputUUIDPair inputUUIDPair : inputUUIDPairList){
            Node<?> inputNode = getNode(inputUUIDPair.uuid);
            inputNodePairList.add(new InputNodePair(inputUUIDPair.inputName, inputNode));
        }
        return inputNodePairList;
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
