package com.aegeanflow.essentials.example;

import com.aegeanflow.core.exchange.Exchange;
import com.aegeanflow.core.spi.box.AbstractAnnotatedBox;
import com.aegeanflow.core.spi.annotation.NodeConfig;
import com.aegeanflow.core.spi.annotation.NodeEntry;
import com.aegeanflow.core.spi.annotation.NodeInput;

@NodeEntry
public class TestBox extends AbstractAnnotatedBox<String> {

    private String input1;

    private Integer input2;

    private Long inputTestFlow;

    private String config1;

    private Double config2;

    public Exchange<String> call() throws Exception {
        return null;
    }

    @NodeInput
    public void setInputTestFlow(Long inputTestFlow) {
        this.inputTestFlow = inputTestFlow;
    }

    @NodeInput(label = "testC")
    public void setConfig1(String config1) {
        this.config1 = config1;
    }

    @NodeInput
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
