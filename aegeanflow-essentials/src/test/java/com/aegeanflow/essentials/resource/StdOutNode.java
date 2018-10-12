package com.aegeanflow.essentials.resource;

import com.aegeanflow.core.spi.node.AbstractSynchronizedNode;
import com.aegeanflow.core.spi.parameter.Input;
import com.aegeanflow.core.spi.parameter.Output;
import com.aegeanflow.core.spi.parameter.Parameter;
import com.aegeanflow.essentials.table.Row;
import com.aegeanflow.essentials.table.Table;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

public class StdOutNode extends AbstractSynchronizedNode {

    public final static Input<Object> VALUE = Parameter.input("value", Object.class);

    private Object value;

    @Override
    protected void run() {
        System.out.println(value);
    }

    @Override
    protected <T> void setInput(Input<T> input, T value) {
        if (VALUE.equals(input)) {
            this.value = value;
        }
    }

    @Override
    public String getName() {
        return "StdOut";
    }

    @Override
    public Collection<Output<?>> getOutputs() {
        return Collections.EMPTY_LIST;
    }

    @Override
    public Collection<Input<?>> getInputs() {
        return Arrays.asList(VALUE);
    }

}
