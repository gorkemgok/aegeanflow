package com.aegeanflow.application;

import com.google.inject.Guice;
import com.google.inject.Injector;

/**
 * Created by gorkem on 15.01.2018.
 */
public class AegeanFlowStarter {

    public static void main(String[] args) {
        Injector injector = Guice.createInjector(new BootstrapModule());
        AegeanFlowServiceStarter serviceStarter = injector.getInstance(AegeanFlowServiceStarter.class);
        serviceStarter.start();
    }
}
