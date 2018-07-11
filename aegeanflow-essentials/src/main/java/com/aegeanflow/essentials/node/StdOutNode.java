package com.aegeanflow.essentials.node;

import com.aegeanflow.core.spi.AbstractRunnableNode;
import com.aegeanflow.core.spi.annotation.NodeEntry;
import com.aegeanflow.core.spi.annotation.NodeInput;

/**
 * Created by gorkem on 30.01.2018.
 */
@NodeEntry
public class StdOutNode extends AbstractRunnableNode<Void> {

    private Object value;

    @Override
    public Void call() throws Exception {
        System.out.println(value.toString());
        return null;
    }

    @NodeInput
    public void setText(Object value) {
        this.value = value;
    }
}
