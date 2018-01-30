package com.aegeanflow.core.node.data;

import com.aegeanflow.core.spi.annotation.NodeOutput;
import com.aegeanflow.core.spi.annotation.NodeOutputBean;

import java.util.List;

/**
 * Created by gorkem on 29.01.2018.
 */
@NodeOutputBean
public class TabularDataInfo {

    private final TabularData tabularData;

    public TabularDataInfo(TabularData tabularData) {
        this.tabularData = tabularData;
    }

    @NodeOutput
    public Integer getColumnCount() {
        return tabularData.getSchema().getFieldList().size();
    }

    @NodeOutput
    public Integer getRowCount() {
        return tabularData.getData().size();
    }

    @NodeOutput
    public TabularData.Schema getSchema() {
        return tabularData.getSchema();
    }

    @NodeOutput
    public List<List<Object>> getData() {
        return tabularData.getData();
    }


}
