package com.aegeanflow.core.node.data;

import com.aegeanflow.core.spi.annotation.NodeOutput;
import com.aegeanflow.core.spi.annotation.NodeOutputBean;

@NodeOutputBean
public class ApartedTabularData {

    private final TabularData.Schema schema;

    private final SchemalessData data;

    public ApartedTabularData(TabularData.Schema schema, SchemalessData data) {
        this.schema = schema;
        this.data = data;
    }

    @NodeOutput
    public TabularData.Schema getSchema() {
        return schema;
    }

    @NodeOutput
    public SchemalessData getData() {
        return data;
    }
}
