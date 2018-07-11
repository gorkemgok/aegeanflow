package com.aegeanflow.core;

import com.aegeanflow.core.engine.DataFlowEngineManager;
import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Injector;

/**
 * Created by gorkem on 12.01.2018.
 */
public class AegeanFlow {

    private static class AegeanFlowSingletons{
        public final NodeRepository nodeRepository;

        @Inject
        private AegeanFlowSingletons(NodeRepository nodeRepository) {
            this.nodeRepository = nodeRepository;
        }
    }

    private static AegeanFlow aegeanFlow;

    private static final Object LOCK = new Object();

    public static AegeanFlow start(Injector injector){
        if (aegeanFlow == null){
            synchronized (LOCK){
                if (aegeanFlow == null){
                    aegeanFlow = new AegeanFlow(injector);
                }
            }
        }
        return aegeanFlow;
    }

    public static AegeanFlow start(){
        if (aegeanFlow == null){
            synchronized (LOCK){
                if (aegeanFlow == null){
                    Injector injector = Guice.createInjector(new CoreModule());
                    aegeanFlow = new AegeanFlow(injector);
                }
            }
        }
        return aegeanFlow;
    }

    private final Injector injector;

    private final AegeanFlowSingletons singletons;

    private AegeanFlow(Injector injector) {
        this.injector = injector;
        this.singletons = injector.getInstance(AegeanFlowSingletons.class);
    }

    public DataFlowEngineManager createEngineFactory(){
        return injector.getInstance(DataFlowEngineManager.class);
    }

    public NodeRepository getNodeRepository(){
        return singletons.nodeRepository;
    }

    public Injector getInjector() {
        return injector;
    }
}
