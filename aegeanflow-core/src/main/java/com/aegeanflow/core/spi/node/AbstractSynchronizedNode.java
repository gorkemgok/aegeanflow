package com.aegeanflow.core.spi.node;

import com.aegeanflow.core.spi.parameter.Input;

public abstract class AbstractSynchronizedNode extends AbstractNode implements Node {

    @Override
    public <T> void accept(Input<T> input, T value) {
        setInput(input, value);
        getCompletedParameters().add(input);
    }
}
