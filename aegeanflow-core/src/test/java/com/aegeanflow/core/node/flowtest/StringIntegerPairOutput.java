package com.aegeanflow.core.node.flowtest;

import com.aegeanflow.core.spi.annotation.NodeOutput;
import com.aegeanflow.core.spi.annotation.NodeOutputBean;

@NodeOutputBean
public class StringIntegerPairOutput {

    private final String text;

    private final Integer count;

    public StringIntegerPairOutput(String text, Integer count) {
        this.text = text;
        this.count = count;
    }

    @NodeOutput
    public String getText() {
        return text;
    }

    @NodeOutput
    public Integer getCount() {
        return count;
    }
}
