package com.aegeanflow.essentials.box;

import com.aegeanflow.core.Exchange;
import com.aegeanflow.core.spi.AbstractAnnotatedBox;
import com.aegeanflow.essentials.data.ApartedTabularData;
import com.aegeanflow.essentials.data.ApartedTabularDataExchange;
import com.aegeanflow.essentials.data.SchemalessData;
import com.aegeanflow.essentials.data.TabularData;
import com.aegeanflow.core.spi.annotation.NodeEntry;
import com.aegeanflow.core.spi.annotation.NodeInput;

@NodeEntry
public class TabularDataAparter extends AbstractAnnotatedBox<ApartedTabularData> {

    private TabularData input;

    @Override
    public Exchange<ApartedTabularData> call() throws Exception {
        return new ApartedTabularDataExchange(
                new ApartedTabularData(input.getSchema(), new SchemalessData(input.getData())));
    }

    @NodeInput
    public void setInput(TabularData input) {
        this.input = input;
    }
}
