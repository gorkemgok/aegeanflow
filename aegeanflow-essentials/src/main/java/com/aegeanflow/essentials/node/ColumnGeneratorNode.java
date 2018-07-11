package com.aegeanflow.essentials.node;

import com.aegeanflow.essentials.data.TabularData;
import com.aegeanflow.core.spi.AbstractRunnableNode;
import com.aegeanflow.core.spi.annotation.NodeConfig;
import com.aegeanflow.core.spi.annotation.NodeEntry;
import com.aegeanflow.core.spi.annotation.NodeInput;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by gorkem on 30.01.2018.
 */
@NodeEntry(label = "Column Generator")
public class ColumnGeneratorNode extends AbstractRunnableNode<TabularData>{

    private Object seedValue;

    private Integer rowCount;

    private String columnName;

    @Override
    public TabularData call() throws Exception {
        TabularData.Schema schema = new TabularData.Schema(Arrays.asList(new TabularData.Field(columnName, seedValue.getClass())));
        List<List<Object>> data = new ArrayList<>();
        for (int i = 0; i < rowCount; i++) {
            data.add(Arrays.asList(seedValue));
        }
        return new TabularData(schema, data);
    }

    @NodeInput
    public void setSeedValue(Object seedValue) {
        this.seedValue = seedValue;
    }

    @NodeInput
    @NodeConfig
    public void setRowCount(Integer rowCount) {
        this.rowCount = rowCount;
    }

    @NodeConfig
    public void setColumnName(String columnName) {
        this.columnName = columnName;
    }
}
