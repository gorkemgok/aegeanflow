package com.aegeanflow.core.engine;

import com.aegeanflow.core.flow.Flow;
import com.aegeanflow.core.flow.FlowNode;
import com.aegeanflow.core.node.NodeRepository;
import com.aegeanflow.core.spi.Node;
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

    private final NodeRepository nodeRepository;

    @Inject
    public DataFlowEngineFactory(Injector injector, NodeRepository nodeRepository) {
        this.injector = injector;
        this.nodeRepository = nodeRepository;
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
        return new DataFlowEngine(flow, nodeList, stateProvider, nodeRepository);
    }
}
