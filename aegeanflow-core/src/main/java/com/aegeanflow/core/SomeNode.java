package com.aegeanflow.core;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class SomeNode extends AbstractNode{

    public static final Parameter<String> STRING_INPUT = Parameter.of("string_input", String.class);

    public static final Parameter<Integer> INTEGER_INPUT = Parameter.of("integer_input", Integer.class);

    public static final Parameter<String> STRING_OUTPUT = Parameter.of("string_output", String.class);

    private String stringInput;

    private Integer integerInput;

    @Override
    public void run() {
        router.next(STRING_OUTPUT, stringInput + "_" + integerInput);
        System.out.println(stringInput + "_" + integerInput);
    }

    @Override
    public <T> void accept(Parameter<T> input, T value) {
        super.accept(input, value);
        if (input.equals(STRING_INPUT)) {
            stringInput = (String) value;
        } else if (input.equals(INTEGER_INPUT)) {
            integerInput = (Integer) value;
        }
    }

    @Override
    public Set<Parameter> listParameters() {
        return new HashSet<>(Arrays.asList(STRING_INPUT, INTEGER_INPUT));
    }

}
