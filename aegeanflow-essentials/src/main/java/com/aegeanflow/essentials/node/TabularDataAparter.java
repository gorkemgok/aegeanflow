package com.aegeanflow.essentials.node;

import com.aegeanflow.essentials.data.ApartedTabularData;
import com.aegeanflow.essentials.data.SchemalessData;
import com.aegeanflow.essentials.data.TabularData;
import com.aegeanflow.core.spi.AbstractRunnableNode;
import com.aegeanflow.core.spi.annotation.NodeEntry;
import com.aegeanflow.core.spi.annotation.NodeInput;

@NodeEntry
public class TabularDataAparter extends AbstractRunnableNode<ApartedTabularData> {

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
