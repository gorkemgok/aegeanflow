package com.aegeanflow.core;

import com.aegeanflow.core.spi.Plugin;
import com.google.inject.AbstractModule;
import com.google.inject.multibindings.Multibinder;

public class TestModule extends AbstractModule {
    @Override
    protected void configure() {
        Multibinder<Plugin> pluginMultibinder = Multibinder.newSetBinder(binder(), Plugin.class);
        pluginMultibinder.addBinding().to(TestPlugin.class);
    }
}
