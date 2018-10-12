package com.aegeanflow.core;

import com.aegeanflow.core.box.BoxRepository;
import com.aegeanflow.core.ioc.AegeanFlowCoreModule;
import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Injector;

/**
 * Created by gorkem on 12.01.2018.
 */
public class AegeanFlow {

    private static class AegeanFlowSingletons{
        public final BoxRepository boxRepository;

        @Inject
        private AegeanFlowSingletons(BoxRepository boxRepository) {
            this.boxRepository = boxRepository;
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
                    Injector injector = Guice.createInjector(new AegeanFlowCoreModule());
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

    public BoxRepository getBoxRepository(){
        return singletons.boxRepository;
    }

    public Injector getInjector() {
        return injector;
    }
}
