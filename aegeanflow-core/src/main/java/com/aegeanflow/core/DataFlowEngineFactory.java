package com.aegeanflow.core;

import com.aegeanflow.core.flow.Flow;
import com.aegeanflow.core.flow.FlowNode;
import com.google.inject.Inject;
import com.google.inject.Injector;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by gorkem on 12.01.2018.
 */
public class DataFlowEngineFactory {

    private final Injector injector;

    private final List<CompiledNodeInfo> compiledNodeInfoList;

    @Inject
    public DataFlowEngineFactory(Injector injector, List<CompiledNodeInfo> compiledNodeInfoList) {
        this.injector = injector;
        this.compiledNodeInfoList = compiledNodeInfoList;
    }

    public DataFlowEngine create(Flow flow) throws ClassNotFoundException {
        return create(flow, null);
    }

    public DataFlowEngine create(Flow flow, @Nullable DataFlowEngine stateProvider) throws ClassNotFoundException {
        List<Node<?>> nodeList = new ArrayList<>();
        for (FlowNode flowNode : flow.getNodeList()){
            Class<?> nodeClass = Class.forName(flowNode.getNodeClass());
            Node node = (Node) injector.getInstance(nodeClass);
            node.setUUID(flowNode.getUUID());
            nodeList.add(node);
        }
        return new DataFlowEngine(flow, nodeList, stateProvider, compiledNodeInfoList);
    }
}
