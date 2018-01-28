package com.aegeanflow.core.node;

import com.aegeanflow.core.spi.AbstractNode;
import com.aegeanflow.core.spi.annotation.NodeConfig;
import com.aegeanflow.core.spi.annotation.NodeEntry;
import com.aegeanflow.core.spi.annotation.NodeInput;

import java.util.List;
import java.util.StringJoiner;

@NodeEntry
public class TestNode2IN2OUT extends AbstractNode<TestNodeOutput> {

    public static final String NODE_CONG_NAME = "Config List";

    private Integer count;

    private String text;

    private Long someConfig;

    private List<Long> someConfigList;

    public Integer getCount() {
        return count;
    }

    public Long getSomeConfig() {
        return someConfig;
    }

    @NodeConfig(order = 1)
    public void setSomeConfig(Long someConfig) {
        this.someConfig = someConfig;
    }

    public List<Long> getSomeConfigList() {
        return someConfigList;
    }

    @NodeConfig(label = NODE_CONG_NAME)
    public void setSomeConfigList(List<Long> someConfigList) {
        this.someConfigList = someConfigList;
    }

    @NodeInput(order = 2)
    public void setCount(Integer count) {
        this.count = count;
    }

    public String getText() {
        return text;
    }

    @NodeInput(order = 1)
    public void setText(String text) {
        this.text = text;
    }

    @Override
    public TestNodeOutput call() throws Exception {
        StringJoiner sj = new StringJoiner(",");
        for (int i = 0; i < count; i++) {
            sj.add(text);
        }
        TestNodeOutput tno = new TestNodeOutput(text, sj.toString());
        return tno;
    }
}

