package com.aegeanflow.core.node;

import com.aegeanflow.core.node.data.TabularData;
import com.aegeanflow.core.node.data.TabularDataInfo;
import com.aegeanflow.core.spi.AbstractRunnableNode;
import com.aegeanflow.core.spi.annotation.NodeEntry;
import com.aegeanflow.core.spi.annotation.NodeInput;

/**
 * Created by gorkem on 29.01.2018.
 */
@NodeEntry(label = "Tabular Data Info")
public class TabularDataInfoNode extends AbstractRunnableNode<TabularDataInfo> {

    private TabularData tabularData;

    @Override
    public TabularDataInfo call() throws Exception {
        return new TabularDataInfo(tabularData);
    }

    @NodeInput
    public void setTabularData(TabularData tabularData) {
        this.tabularData = tabularData;
    }
}