package com.aegeanflow.essentials.node;

import com.aegeanflow.essentials.data.SchemalessData;
import com.aegeanflow.essentials.data.TabularData;
import com.aegeanflow.core.spi.AbstractRunnableNode;
import com.aegeanflow.core.spi.annotation.NodeEntry;
import com.aegeanflow.core.spi.annotation.NodeInput;

@NodeEntry
public class DataSchemarNode extends AbstractRunnableNode<TabularData> {

    private SchemalessData data;

    private TabularData.Schema schema;

    @Override
    public TabularData call() throws Exception {
        return new TabularData(schema, data.getData());
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
