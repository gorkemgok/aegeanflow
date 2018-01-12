package com.aegeanflow.core;

import com.google.inject.Guice;
import com.google.inject.Injector;

/**
 * Created by gorkem on 12.01.2018.
 */
public class AegeanFlow {

    private final Injector injector;

    private static AegeanFlow aegeanFlow;

    private static final Object LOCK = new Object();
    public static AegeanFlow start(){
        if (aegeanFlow == null){
            synchronized (LOCK){
                if (aegeanFlow == null){
                    Injector injector = Guice.createInjector(new BootstrapModule());
                    aegeanFlow = new AegeanFlow(injector);
                }
            }
        }
        return aegeanFlow;
    }
    private AegeanFlow(Injector injector) {
        this.injector = injector;
    }

    public DataFlowEngineFactory createEngineFactory(){
        return injector.getInstance(DataFlowEngineFactory.class);
    }

    public Injector getInjector() {
        return injector;
    }
}
