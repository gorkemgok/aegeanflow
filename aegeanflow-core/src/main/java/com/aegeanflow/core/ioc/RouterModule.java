package com.aegeanflow.core.ioc;

import com.aegeanflow.core.route.Router;
import com.aegeanflow.core.route.RouterFactory;
import com.google.inject.AbstractModule;
import com.google.inject.assistedinject.FactoryModuleBuilder;

public class RouterModule extends AbstractModule {
    @Override
    protected void configure() {
        install(new FactoryModuleBuilder().implement(Router.class, Router.class).build(RouterFactory.class));
    }
}
