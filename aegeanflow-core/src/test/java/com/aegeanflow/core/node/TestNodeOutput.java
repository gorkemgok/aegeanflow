package com.aegeanflow.core.node;

import com.aegeanflow.core.spi.annotation.NodeOutput;
import com.aegeanflow.core.spi.annotation.NodeOutputBean;

@NodeOutputBean
public class TestNodeOutput {

    private final String first;

    private final String second;

    private String combine;

    public TestNodeOutput(String first, String second) {
        this.first = first;
        this.second = second;
        this.combine = first + second;
    }

    @NodeOutput(order = 1)
    public String getFirst() {
        return first;
    }

    @NodeOutput
    public String getSecond() {
        return second;
    }

    public String getCombine() {
        return combine;
    }
}
