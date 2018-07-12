package com.aegeanflow.essentials.data;

import com.aegeanflow.core.Exchange;
import com.aegeanflow.core.spi.annotation.NodeOutput;
import com.aegeanflow.core.spi.annotation.NodeOutputBean;

@NodeOutputBean
public class SplittedTabularData {

    private final TabularData train;

    private final TabularData crossValidation;

    private final TabularData test;

    public SplittedTabularData(TabularData train, TabularData crossValidation, TabularData test) {
        this.train = train;
        this.crossValidation = crossValidation;
        this.test = test;
    }

    @NodeOutput
    public Exchange<TabularData> getTrain() {
        return new TabularDataExchange(train);
    }

    @NodeOutput
    public Exchange<TabularData> getCrossValidation() {
        return new TabularDataExchange(crossValidation);
    }

    @NodeOutput
    public Exchange<TabularData> getTest() {
        return new TabularDataExchange(test);
    }
}
