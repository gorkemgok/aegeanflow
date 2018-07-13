package com.aegeanflow.core.node;

import com.aegeanflow.core.exchange.Exchange;
import com.aegeanflow.core.spi.AbstractAnnotatedBox;
import com.aegeanflow.core.spi.annotation.NodeEntry;
import com.aegeanflow.core.spi.annotation.NodeInput;

@NodeEntry
public class MathAddBox extends AbstractAnnotatedBox<Double> {

    private Double input1;

    private Double input2;

    @Override
    public Exchange<Double> call() throws Exception {
        return Exchange.of(input1 + input2);
    }

    @NodeInput
    public void setInput1(Double input1) {
        this.input1 = input1;
    }

    @NodeInput
    public void setInput2(Double input2) {
        this.input2 = input2;
    }
}
