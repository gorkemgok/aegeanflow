package com.aegeanflow.core.plugin.datasource;

import javax.sql.DataSource;
import java.util.*;

public class DatasourceManager {

    private final Map<String, DataSource> dataSourceMap;

    public DatasourceManager() {
        this.dataSourceMap = new HashMap<>();
    }

    public void register(String name, DataSource dataSource) {
        dataSourceMap.put(name, dataSource);
    }

    public DataSource get(String name) {
        return dataSourceMap.get(name);
    }
}
