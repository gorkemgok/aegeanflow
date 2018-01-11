package com.aegeanflow.core;

import com.aegeanflow.core.annotation.NodeConfig;
import com.aegeanflow.core.annotation.NodeEntry;
import com.aegeanflow.core.annotation.NodeInput;

@NodeEntry
public class TestNode extends AbstractNode<String> {

    private String input1;

    private Integer input2;

    private String config1;

    private Double config2;

    public String call() throws Exception {
        return null;
    }

    @NodeConfig(label = "testC")
    public void setConfig1(String config1) {
        this.config1 = config1;
    }

    @NodeConfig
    public void setConfig2(Double config2) {
        this.config2 = config2;
    }

    @NodeInput(label = "TestI")
    public void setInput1(String input1) {
        this.input1 = input1;
    }

    @NodeInput
    public void setInput2(Integer input2) {
        this.input2 = input2;
    }
}
