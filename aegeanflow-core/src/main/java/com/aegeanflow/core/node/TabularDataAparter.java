package com.aegeanflow.core.node;

import com.aegeanflow.core.node.data.ApartedTabularData;
import com.aegeanflow.core.node.data.SchemalessData;
import com.aegeanflow.core.node.data.TabularData;
import com.aegeanflow.core.spi.AbstractNode;
import com.aegeanflow.core.spi.annotation.NodeEntry;
import com.aegeanflow.core.spi.annotation.NodeInput;

@NodeEntry
public class TabularDataAparter extends AbstractNode<ApartedTabularData> {

    private TabularData input;

    @Override
    public ApartedTabularData call() throws Exception {
        return new ApartedTabularData(input.getSchema(), new SchemalessData(input.getData()));
    }

    @NodeInput
    public void setInput(TabularData input) {
        this.input = input;
    }
}
