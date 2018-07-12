package com.aegeanflow.core.engine;

import com.aegeanflow.core.NodeRepository;
import com.aegeanflow.core.flow.Flow;
import com.aegeanflow.core.flow.FlowNode;
import com.aegeanflow.core.spi.AnnotatedBox;
import com.google.inject.Inject;
import com.google.inject.Injector;

import javax.annotation.Nullable;
import java.util.*;

/**
 * Created by gorkem on 12.01.2018.
 */
public class DataFlowEngineManager {

    private final Injector injector;

    private final NodeRepository nodeRepository;

    private final Map<UUID, DataFlowEngine> engineMap;

    @Inject
    public DataFlowEngineManager(Injector injector, NodeRepository nodeRepository) {
        this.injector = injector;
        this.nodeRepository = nodeRepository;
        this.engineMap = new Hashtable<>();
    }

    public DataFlowEngine create(Flow flow, boolean reset) throws ClassNotFoundException {
        if (flow.getUuid() == null) {
            flow.setUuid(UUID.randomUUID());
        }
        DataFlowEngine prevDFE = null;
        if (!reset) {
            prevDFE = engineMap.get(flow.getUuid());
        }
        DataFlowEngine dataFlowEngine = create(flow, prevDFE);
        engineMap.put(flow.getUuid(), dataFlowEngine);
        return dataFlowEngine;
    }

    public DataFlowEngine create(Flow flow, @Nullable DataFlowEngine stateProvider) throws ClassNotFoundException {
        List<AnnotatedBox<?>> annotatedBoxList = new ArrayList<>();
        for (FlowNode flowNode : flow.getNodeList()){
            AnnotatedBox annotatedBox = injector.getInstance(flowNode.getNodeClass());
            annotatedBox.setUUID(flowNode.getUUID());
            annotatedBox.setName(flowNode.getName());
            annotatedBoxList.add(annotatedBox);
        }
        return new DataFlowEngine(flow, annotatedBoxList, nodeRepository, stateProvider);
    }
}
