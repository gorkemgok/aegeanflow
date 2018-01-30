package com.aegeanflow.core.node;

import com.aegeanflow.core.spi.AbstractRunnableNode;
import com.aegeanflow.core.spi.annotation.NodeEntry;
import com.aegeanflow.core.spi.annotation.NodeInput;

import java.util.StringJoiner;

@NodeEntry(label = TestNode2IN1OUT.NODE_LABEL)
public class TestNode2IN1OUT extends AbstractRunnableNode<String> {

    public static final String NODE_LABEL = "Test RunnableNode 2 IN 1 OUT";
    public static final String COUNT_IN_LABEL = "Count";

    private Integer count;

    private String text;

    public Integer getCount() {
        return count;
    }

    @NodeInput(label = TestNode2IN1OUT.COUNT_IN_LABEL)
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
    public String call() throws Exception {
        StringJoiner sj = new StringJoiner(",");
        for (int i = 0; i < count; i++) {
            sj.add(text);
        }
        return sj.toString();
    }
}
