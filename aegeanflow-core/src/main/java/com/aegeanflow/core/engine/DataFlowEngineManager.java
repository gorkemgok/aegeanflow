package com.aegeanflow.core.engine;

import com.aegeanflow.core.box.BoxRepository;
import com.aegeanflow.core.model.SessionModel;
import com.aegeanflow.core.model.NodeModel;
import com.aegeanflow.core.spi.box.AnnotatedBox;
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

    public DataFlowEngine create(SessionModel sessionModel, boolean reset) throws ClassNotFoundException {
        if (sessionModel.getUuid() == null) {
            //sessionProxy.setUuid(UUID.randomUUID());
        }
        DataFlowEngine prevDFE = null;
        if (!reset) {
            prevDFE = engineMap.get(sessionModel.getUuid());
        }
        DataFlowEngine dataFlowEngine = create(sessionModel, prevDFE);
        engineMap.put(sessionModel.getUuid(), dataFlowEngine);
        return dataFlowEngine;
    }

    public DataFlowEngine create(SessionModel sessionModel, @Nullable DataFlowEngine stateProvider) throws ClassNotFoundException {
        List<AnnotatedBox<?>> annotatedBoxList = new ArrayList<>();
        for (NodeModel nodeModel : sessionModel.getNodes()){
            AnnotatedBox annotatedBox = injector.getInstance(nodeModel.getBoxType());
            annotatedBox.setUUID(nodeModel.getUUID());
            annotatedBox.setName(nodeModel.getLabel());
            annotatedBoxList.add(annotatedBox);
        }
        return new DataFlowEngine(sessionModel, annotatedBoxList, boxRepository, stateProvider);
    }
}
