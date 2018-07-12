package com.aegeanflow.essentials;

import com.aegeanflow.core.spi.Plugin;
import com.aegeanflow.essentials.data.Convertor;
import com.aegeanflow.essentials.data.ResultSetToTabularData;
import com.aegeanflow.essentials.data.TabularData;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.google.inject.multibindings.Multibinder;

import java.sql.ResultSet;

public class EssentialsModule extends AbstractModule{
    @Override
    protected void configure() {
        Multibinder<Plugin> pluginMultibinder = Multibinder.newSetBinder(binder(), Plugin.class);
        pluginMultibinder.addBinding().to(EssentialsPlugin.class);
    }

    @Provides
    @Singleton
    Convertor<ResultSet, TabularData> provideRsToTDConvertor() {
        return new ResultSetToTabularData();
    }
}
