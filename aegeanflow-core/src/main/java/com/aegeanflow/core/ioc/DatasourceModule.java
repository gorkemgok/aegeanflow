package com.aegeanflow.core.ioc;

import com.aegeanflow.core.plugin.datasource.DatasourceFactory;
import com.aegeanflow.core.plugin.datasource.DatasourceManager;
import com.google.inject.AbstractModule;
import com.google.inject.Scopes;

public class DatasourceModule extends AbstractModule {
    @Override
    protected void configure() {
        bind(DatasourceManager.class).in(Scopes.SINGLETON);
        bind(DatasourceFactory.class).in(Scopes.SINGLETON);
    }
}
