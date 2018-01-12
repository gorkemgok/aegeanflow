package com.aegeanflow.core.example;

import com.aegeanflow.core.AbstractNode;
import com.aegeanflow.core.annotation.NodeConfig;
import com.aegeanflow.core.annotation.NodeEntry;

/**
 * Created by gorkem on 12.01.2018.
 */
@NodeEntry
public class StringSourceNode extends AbstractNode<String> {

    private String prefix;

    @Override
    public String call() throws Exception {
        return prefix + "-string";
    }

    @NodeConfig
    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }
}
