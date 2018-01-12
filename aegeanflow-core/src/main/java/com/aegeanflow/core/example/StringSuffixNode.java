package com.aegeanflow.core.example;

import com.aegeanflow.core.AbstractNode;
import com.aegeanflow.core.annotation.NodeConfig;
import com.aegeanflow.core.annotation.NodeEntry;
import com.aegeanflow.core.annotation.NodeInput;

/**
 * Created by gorkem on 12.01.2018.
 */
@NodeEntry
public class StringSuffixNode extends AbstractNode<String>{

    private String suffix;

    private String input;

    @Override
    public String call() throws Exception {
        return input + "-" + suffix;
    }

    @NodeInput
    public void setInput(String input) {
        this.input = input;
    }

    @NodeConfig
    public void setSuffix(String suffix) {
        this.suffix = suffix;
    }
}
