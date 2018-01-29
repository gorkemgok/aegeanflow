package com.aegeanflow.core.node;

import com.aegeanflow.core.node.data.SchemalessData;
import com.aegeanflow.core.node.data.TabularData;
import com.aegeanflow.core.spi.AbstractNode;
import com.aegeanflow.core.spi.annotation.NodeEntry;
import com.aegeanflow.core.spi.annotation.NodeInput;

@NodeEntry
public class DataSchemarNode extends AbstractNode<TabularData>{

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
