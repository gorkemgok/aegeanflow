package com.aegeanflow.core.node.data;

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
    public TabularData getTrain() {
        return train;
    }

    @NodeOutput
    public TabularData getCrossValidation() {
        return crossValidation;
    }

    @NodeOutput
    public TabularData getTest() {
        return test;
    }
}
