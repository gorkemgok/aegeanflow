package com.aegeanflow.essentials.box;

import com.aegeanflow.core.exchange.Exchange;
import com.aegeanflow.core.spi.box.AbstractAnnotatedBox;
import com.aegeanflow.essentials.data.TabularData;
import com.aegeanflow.core.spi.annotation.NodeConfig;
import com.aegeanflow.core.spi.annotation.NodeEntry;
import com.aegeanflow.core.spi.annotation.NodeInput;
import com.aegeanflow.essentials.data.TabularDataExchange;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by gorkem on 30.01.2018.
 */
@NodeEntry(name = "Column Generator")
public class ColumnGeneratorBox extends AbstractAnnotatedBox<TabularData> {

    private Object seedValue;

    private Integer rowCount;

    private String columnName;

    @Override
    public Exchange<TabularData> call() throws Exception {
        TabularData.Schema schema = new TabularData.Schema(Arrays.asList(new TabularData.Field(columnName, seedValue.getClass())));
        List<List<Object>> data = new ArrayList<>();
        for (int i = 0; i < rowCount; i++) {
            data.add(Arrays.asList(seedValue));
        }
        return new TabularDataExchange(new TabularData(schema, data));
    }

    @NodeInput
    public void setSeedValue(Object seedValue) {
        this.seedValue = seedValue;
    }

    @NodeInput
    public void setRowCount(Integer rowCount) {
        this.rowCount = rowCount;
    }

    @NodeInput
    public void setColumnName(String columnName) {
        this.columnName = columnName;
    }
}
