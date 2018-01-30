package com.aegeanflow.core.node;

import com.aegeanflow.core.node.data.SplittedTabularData;
import com.aegeanflow.core.node.data.TabularData;
import com.aegeanflow.core.spi.AbstractRunnableNode;
import com.aegeanflow.core.spi.annotation.NodeEntry;
import com.aegeanflow.core.spi.annotation.NodeInput;

@NodeEntry(label = "Tabular Data Splitter")
public class TabularDataSplitterNode extends AbstractRunnableNode<SplittedTabularData> {

    private TabularData input;

    @Override
    public SplittedTabularData call() throws Exception {
        return new SplittedTabularData(input, input, input);
    }

    @NodeInput
    public void setInput(TabularData input) {
        this.input = input;
    }
}
