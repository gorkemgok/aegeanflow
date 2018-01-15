package com.aegeanflow.rest;

import com.aegeanflow.core.spi.AegeanFlowService;
import com.google.inject.AbstractModule;
import com.google.inject.Singleton;
import com.google.inject.multibindings.Multibinder;
import com.gorkemgok.annoconf.annotation.LoadService;

/**
 * Created by gorkem on 15.01.2018.
 */
@LoadService(
        ifConfig = "load.rest",
        equalsTo = "true",
        autoLoad = true,
        implementationOf = RestModule.NAME
)
public class RestModule extends AbstractModule {

    public static final String NAME = "restModule";

    @Override
    protected void configure() {
        Multibinder<AegeanFlowService> serviceMultibinder = Multibinder.newSetBinder(binder(), AegeanFlowService.class);
        serviceMultibinder.addBinding().to(RestService.class).in(Singleton.class);
    }
}
