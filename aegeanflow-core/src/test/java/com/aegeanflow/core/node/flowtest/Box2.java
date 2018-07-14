package com.aegeanflow.core.node.flowtest;

import com.aegeanflow.core.exchange.Exchange;
import com.aegeanflow.core.spi.annotation.NodeInput;
import com.aegeanflow.core.spi.box.AbstractAnnotatedBox;
import com.aegeanflow.core.spi.annotation.NodeEntry;

import static com.aegeanflow.core.box.definition.BoxIODefinition.InputType.CONGIF;

@NodeEntry
public class Box2 extends AbstractAnnotatedBox<StringIntegerPairOutput> {

    private String text;

    private Integer length;
    @Override
    public Exchange<StringIntegerPairOutput> call() throws Exception {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            sb.append(text);
        }
        return Exchange.createUnpersistent(new StringIntegerPairOutput(sb.toString(), length));
    }

    @NodeInput(inputType = CONGIF)
    public void setLength(Integer length) {
        this.length = length;
    }

    @NodeInput(inputType = CONGIF)
    public void setText(String text) {
        this.text = text;
    }

}
