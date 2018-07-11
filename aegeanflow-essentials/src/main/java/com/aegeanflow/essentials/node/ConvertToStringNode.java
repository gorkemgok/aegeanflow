package com.aegeanflow.essentials.node;

import com.aegeanflow.core.spi.AbstractRunnableNode;
import com.aegeanflow.core.spi.annotation.NodeEntry;
import com.aegeanflow.core.spi.annotation.NodeInput;

/**
 * Created by gorkem on 29.01.2018.
 */
@NodeEntry(label = "Convert to String")
public class ConvertToStringNode extends AbstractRunnableNode<String> {

    private Object input;

    @Override
    public String call() throws Exception {
        return input.toString();
    }

    @NodeInput
    public void setInput(Object input) {
        this.input = input;
    }
}
