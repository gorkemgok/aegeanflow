package com.aegeanflow.essentials.box;

import com.aegeanflow.core.exchange.Exchange;
import com.aegeanflow.essentials.data.SchemalessData;
import com.aegeanflow.essentials.data.TabularData;
import com.aegeanflow.core.spi.AbstractAnnotatedBox;
import com.aegeanflow.core.spi.annotation.NodeEntry;
import com.aegeanflow.core.spi.annotation.NodeInput;
import com.aegeanflow.essentials.data.TabularDataExchange;

@NodeEntry
public class DataSchemarBox extends AbstractAnnotatedBox<TabularData> {

    private SchemalessData data;

    private TabularData.Schema schema;

    @Override
    public Exchange<TabularData> call() throws Exception {
        return new TabularDataExchange(new TabularData(schema, data.getData()));
    }

    @NodeInput
    public void setData(SchemalessData data) {
        this.data = data;
    }

    @NodeInput
    public void setSchema(TabularData.Schema schema) {
        this.schema = schema;
    }
}
