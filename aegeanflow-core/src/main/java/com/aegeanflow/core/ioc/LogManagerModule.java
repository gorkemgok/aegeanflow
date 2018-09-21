package com.aegeanflow.core.ioc;

import com.aegeanflow.core.logger.SessionLogManager;
import com.aegeanflow.core.logger.SessionLogManagerFactory;
import com.google.inject.AbstractModule;
import com.google.inject.assistedinject.FactoryModuleBuilder;

public class LogManagerModule extends AbstractModule {
    @Override
    protected void configure() {
        install(new FactoryModuleBuilder().implement(SessionLogManager.class, SessionLogManager.class)
                .build(SessionLogManagerFactory.class));
    }
}
