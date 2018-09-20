package com.aegeanflow.core.plugin.datasource;

import com.google.inject.Inject;

import javax.sql.DataSource;

public class DatasourceFactory {

    private final DatasourceManager datasourceManager;

    @Inject
    public DatasourceFactory(DatasourceManager datasourceManager) {
        this.datasourceManager = datasourceManager;
    }

    public DataSource get(String name){
        return datasourceManager.get(name);
    }
}
