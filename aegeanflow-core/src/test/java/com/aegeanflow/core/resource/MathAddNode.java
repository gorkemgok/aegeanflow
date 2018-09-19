package com.aegeanflow.core.resource;

import com.aegeanflow.core.route.tunnel.StreamTunnel;
import com.aegeanflow.core.spi.node.AbstractSynchronizedNode;
import com.aegeanflow.core.spi.parameter.Parameter;
import com.aegeanflow.core.spi.parameter.Input;
import com.aegeanflow.core.spi.parameter.Output;
import com.aegeanflow.core.table.*;

import java.util.Arrays;
import java.util.Collection;

public class MathAddNode extends AbstractSynchronizedNode {

    public static final String NAME = "Math Addition";

    public static final Input<Double> IN_1 = Parameter.input("input1", Double.TYPE);
    public static final Input<Double> IN_2 = Parameter.input("input2", Double.TYPE);
    public static final Output<Double> OUT_2 = Parameter.output("output2", Double.TYPE);

    private Double input1, input2;

    @Override
    protected <T> void setInput(Input<T> input, T value) {
        if (input.isAssignable(IN_1)) {
            input1 = (Double) value;
        } else if (input.isAssignable(IN_2)) {
            input2 = (Double) value;
        }
    }

    @Override
    public void run() {
        Double total = input1 + input2;
        router.next(OUT_2, total);
    }

    @Override
    public Collection<Output<?>> getOutputs() {
        return Arrays.asList(OUT_2);
    }

    @Override
    public Collection<Input<?>> getInputs() {
        return Arrays.asList(IN_1, IN_2);
    }

    @Override
    public String getName() {
        return NAME;
    }
}
