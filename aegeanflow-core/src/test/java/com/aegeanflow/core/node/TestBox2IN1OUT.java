package com.aegeanflow.core.node;

import com.aegeanflow.core.exchange.Exchange;
import com.aegeanflow.core.spi.box.AbstractAnnotatedBox;
import com.aegeanflow.core.spi.annotation.NodeEntry;
import com.aegeanflow.core.spi.annotation.NodeInput;

import java.util.StringJoiner;

@NodeEntry(name = TestBox2IN1OUT.NODE_LABEL)
public class TestBox2IN1OUT extends AbstractAnnotatedBox<String> {

    public static final String NODE_LABEL = "Test AnnotatedNode 2 IN 1 OUT";
    public static final String COUNT_IN_LABEL = "Count";

    private Integer count;

    private String text;

    public Integer getCount() {
        return count;
    }

    @NodeInput(label = TestBox2IN1OUT.COUNT_IN_LABEL)
    public void setCount(Integer count) {
        this.count = count;
    }

    public String getText() {
        return text;
    }

    @NodeInput
    public void setText(String text) {
        this.text = text;
    }

    @Override
    public Exchange<String> call() throws Exception {
        StringJoiner sj = new StringJoiner(",");
        for (int i = 0; i < count; i++) {
            sj.add(text);
        }
        return Exchange.of(sj.toString());
    }
}
