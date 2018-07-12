package com.aegeanflow.core.node.flowtest;

import com.aegeanflow.core.Exchange;
import com.aegeanflow.core.spi.AbstractAnnotatedBox;
import com.aegeanflow.core.spi.annotation.NodeConfig;
import com.aegeanflow.core.spi.annotation.NodeEntry;

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
        return Exchange.create(new StringIntegerPairOutput(sb.toString(), length));
    }

    @NodeConfig
    public void setLength(Integer length) {
        this.length = length;
    }

    @NodeConfig
    public void setText(String text) {
        this.text = text;
    }

}
