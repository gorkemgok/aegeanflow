package com.aegeanflow.core.engine;

import com.aegeanflow.core.BoxRepository;
import com.aegeanflow.core.proxy.SessionProxy;
import com.aegeanflow.core.proxy.NodeProxy;
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

    private final BoxRepository boxRepository;

    private final Map<UUID, DataFlowEngine> engineMap;

    @Inject
    public DataFlowEngineManager(Injector injector, BoxRepository boxRepository) {
        this.injector = injector;
        this.boxRepository = boxRepository;
        this.engineMap = new Hashtable<>();
    }

    public DataFlowEngine create(SessionProxy sessionProxy, boolean reset) throws ClassNotFoundException {
        if (sessionProxy.getUuid() == null) {
            //sessionProxy.setUuid(UUID.randomUUID());
        }
        DataFlowEngine prevDFE = null;
        if (!reset) {
            prevDFE = engineMap.get(sessionProxy.getUuid());
        }
        DataFlowEngine dataFlowEngine = create(sessionProxy, prevDFE);
        engineMap.put(sessionProxy.getUuid(), dataFlowEngine);
        return dataFlowEngine;
    }

    public DataFlowEngine create(SessionProxy sessionProxy, @Nullable DataFlowEngine stateProvider) throws ClassNotFoundException {
        List<AnnotatedBox<?>> annotatedBoxList = new ArrayList<>();
        for (NodeProxy nodeProxy : sessionProxy.getNodeList()){
            AnnotatedBox annotatedBox = injector.getInstance(nodeProxy.getType());
            annotatedBox.setUUID(nodeProxy.getUUID());
            annotatedBox.setName(nodeProxy.getName());
            annotatedBoxList.add(annotatedBox);
        }
        return new DataFlowEngine(sessionProxy, annotatedBoxList, boxRepository, stateProvider);
    }
}
