package com.aegeanflow.essentials.box;

import com.aegeanflow.core.exchange.Exchange;
import com.aegeanflow.core.spi.AbstractAnnotatedBox;
import com.aegeanflow.essentials.data.TabularData;
import com.aegeanflow.essentials.data.TabularDataInfo;
import com.aegeanflow.core.spi.annotation.NodeEntry;
import com.aegeanflow.core.spi.annotation.NodeInput;
import com.aegeanflow.essentials.data.TabularDataInfoExchange;

/**
 * Created by gorkem on 29.01.2018.
 */
@NodeEntry(name = "Tabular Data Info")
public class TabularDataInfoBox extends AbstractAnnotatedBox<TabularDataInfo> {

    private TabularData tabularData;

    @Override
    public Exchange<TabularDataInfo> call() throws Exception {
        return new TabularDataInfoExchange(new TabularDataInfo(tabularData));
    }

    @NodeInput
    public void setTabularData(TabularData tabularData) {
        this.tabularData = tabularData;
    }
}
