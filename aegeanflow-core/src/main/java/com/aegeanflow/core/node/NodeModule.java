package com.aegeanflow.core.node;

import com.aegeanflow.core.node.data.Convertor;
import com.aegeanflow.core.node.data.ResultSetToTabularData;
import com.aegeanflow.core.node.data.TabularData;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;

import java.sql.ResultSet;

/**
 * Created by gorkem on 18.01.2018.
 */
public class NodeModule extends AbstractModule {
    @Override
    protected void configure() {

    }

    @Provides
    @Singleton
    Convertor<ResultSet, TabularData> provideResultSetToTabularConvertor(){
        return new ResultSetToTabularData();
    }
}
