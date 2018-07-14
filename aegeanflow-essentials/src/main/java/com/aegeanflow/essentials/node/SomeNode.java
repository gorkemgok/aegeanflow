package com.aegeanflow.essentials.node;

import com.aegeanflow.core.exchange.Exchange;
import com.aegeanflow.core.spi.node.AbstractSynchronizedNode;
import com.aegeanflow.core.spi.parameter.Parameter;
import com.aegeanflow.core.spi.parameter.Input;
import com.aegeanflow.core.spi.parameter.Output;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class SomeNode extends AbstractSynchronizedNode {

    public static final String NAME = "Some Node (Test Purpose)";

    public static final Input<String> STRING_INPUT = Parameter.input("string_input", String.class);

    public static final Input<Integer> INTEGER_INPUT = Parameter.input("integer_input", Integer.class);

    public static final Output<String> STRING_OUTPUT = Parameter.output("string_output", String.class);

    private String stringInput;

    private Integer integerInput;

    @Override
    public void run() {
        router.next(STRING_OUTPUT, Exchange.of(stringInput + "_" + integerInput));
        System.out.println(stringInput + "_" + integerInput);
    }

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public Collection<Output<?>> getOutputs() {
        return Arrays.asList(STRING_OUTPUT);
    }

    @Override
    protected <T> void setInput(Input<T> input, T value) {
        if (input.equals(STRING_INPUT)) {
            stringInput = (String) value;
        } else if (input.equals(INTEGER_INPUT)) {
            integerInput = (Integer) value;
        }
    }

    @Override
    public Set<Input<?>> getInputs() {
        return new HashSet<>(Arrays.asList(STRING_INPUT, INTEGER_INPUT));
    }

}
