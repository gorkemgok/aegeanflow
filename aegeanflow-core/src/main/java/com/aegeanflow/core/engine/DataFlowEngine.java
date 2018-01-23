package com.aegeanflow.core.engine;

import com.aegeanflow.core.CompiledNodeInfo;
import com.aegeanflow.core.NodeStatus;
import com.aegeanflow.core.exception.NoSuchNodeException;
import com.aegeanflow.core.exception.NodeRuntimeException;
import com.aegeanflow.core.flow.Flow;
import com.aegeanflow.core.flow.FlowNode;
import com.aegeanflow.core.node.NodeRepository;
import com.aegeanflow.core.spi.Node;
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

    public DataFlowEngine(Flow flow, List<Node<?>> nodeList, @Nullable DataFlowEngine stateProvider, NodeRepository nodeRepository) {
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
                List<InputNodePair> inputNodePairList = getInputNodes(node.getUUID());
                List<InputFuture> inputFutures = new ArrayList<>();
                for (InputNodePair inputNodePair : inputNodePairList) {
                    FlowFuture inputFuture = runNode(inputNodePair.node, useState);
                    inputFutures.add(new InputFuture(inputNodePair.inputName, inputFuture));
                }

                List <FlowInput> inputList = new ArrayList<>();
                for (InputFuture inputFuture : inputFutures){
                    try {
                        Object input = inputFuture.get();
                        updateStatus(inputFuture.getUUID(), NodeStatus.DONE);
                        inputList.add(new FlowInput(inputFuture.getInputName(), input));
                    }catch (ExecutionException e){
                        updateStatus(inputFuture.getUUID(), NodeStatus.FAILED);
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
            //SET STATUS AS CANCELLED for WAITIN and RUNNING Nodes
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
            Optional<CompiledNodeInfo> compiledNodeInfoOptional = nodeRepository.getCompiledNodeInfoList().stream()
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
            Optional<CompiledNodeInfo> compiledNodeInfoOptional = nodeRepository.getCompiledNodeInfoList().stream()
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
                .map(flowConnection -> new InputUUIDPair(flowConnection.getInputName(), flowConnection.getFromUUID()))
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
