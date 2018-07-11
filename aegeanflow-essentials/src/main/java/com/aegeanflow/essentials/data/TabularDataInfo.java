package com.aegeanflow.essentials.data;

import com.aegeanflow.core.spi.annotation.NodeOutput;
import com.aegeanflow.core.spi.annotation.NodeOutputBean;

/**
 * Created by gorkem on 29.01.2018.
 */
@NodeOutputBean
public class TabularDataInfo {

    private final TabularData tabularData;

    public TabularDataInfo(TabularData tabularData) {
        this.tabularData = tabularData;
    }

    @NodeOutput(order = 1)
    public Integer getColumnCount() {
        return tabularData.getSchema().getFieldList().size();
    }

    @NodeOutput(order = 2)
    public Integer getRowCount() {
        return tabularData.getData().size();
    }

    @NodeOutput(order = 3)
    public TabularData.Schema getSchema() {
        return tabularData.getSchema();
    }

    @NodeOutput(order = 4)
    public SchemalessData getData() {
        return new SchemalessData(tabularData.getData());
    }


}
