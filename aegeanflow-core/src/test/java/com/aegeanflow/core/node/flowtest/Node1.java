package com.aegeanflow.core.node.flowtest;

import com.aegeanflow.core.spi.AbstractNode;
import com.aegeanflow.core.spi.annotation.NodeConfig;
import com.aegeanflow.core.spi.annotation.NodeEntry;

@NodeEntry
public class Node1 extends AbstractNode<String>{

    private String seedText;

    @Override
    public String call() throws Exception {
        return seedText;
    }

    @NodeConfig
    public void setSeedText(String seedText) {
        this.seedText = seedText;
    }
}
