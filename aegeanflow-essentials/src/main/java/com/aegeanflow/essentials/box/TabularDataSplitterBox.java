package com.aegeanflow.essentials.box;

import com.aegeanflow.core.exchange.Exchange;
import com.aegeanflow.core.spi.box.AbstractAnnotatedBox;
import com.aegeanflow.essentials.data.SplittedTabularData;
import com.aegeanflow.essentials.data.TabularData;
import com.aegeanflow.core.spi.annotation.NodeEntry;
import com.aegeanflow.core.spi.annotation.NodeInput;

@NodeEntry(name = "Tabular Data Splitter")
public class TabularDataSplitterBox extends AbstractAnnotatedBox<SplittedTabularData> {

    private TabularData input;

    @Override
    public Exchange<SplittedTabularData> call() throws Exception {
        return Exchange.createNonpersistent(new SplittedTabularData(input, input, input));
    }

    @NodeInput
    public void setInput(TabularData input) {
        this.input = input;
    }
}
