package com.aegeanflow.core.session;

import com.aegeanflow.core.ioc.TunnelModule;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Scopes;

import java.util.UUID;


public class SessionModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(SessionContext.class).in(Scopes.SINGLETON);
    }

    @Provides
    UUID provideRandomUUID() {
        return UUID.randomUUID();
    }
}
