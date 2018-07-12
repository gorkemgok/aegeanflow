package com.aegeanflow.essentials.node;

import com.aegeanflow.core.*;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class SomeNode extends AbstractNode {

    public static final NodeId ID = NodeId.of(SomeNode.class);

    public static final Input<String> STRING_INPUT = Parameter.input("string_input", String.class);

    public static final Input<Integer> INTEGER_INPUT = Parameter.input("integer_input", Integer.class);

    public static final Output<String> STRING_OUTPUT = Parameter.output("string_output", String.class);

    private String stringInput;

    private Integer integerInput;

    @Override
    public void run() {
        router.next(STRING_OUTPUT, new StringExchange(stringInput + "_" + integerInput));
        System.out.println(stringInput + "_" + integerInput);
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

    @Override
    public NodeId getId() {
        return ID;
    }

}
