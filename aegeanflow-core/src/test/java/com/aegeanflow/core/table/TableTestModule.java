package com.aegeanflow.core.table;

import com.aegeanflow.core.ioc.CoreModule;
import com.google.inject.AbstractModule;

public class TableTestModule extends AbstractModule {
    @Override
    protected void configure() {
        install(new CoreModule());
    }
}
