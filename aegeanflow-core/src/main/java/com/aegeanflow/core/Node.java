package com.aegeanflow.core;

import java.util.Set;

public interface Node {

    void initialize(Router router);

    void run();

    <T> void accept(Input<T> input, T value);

    default <T> void acceptAndRun(Input<T> input, T value) {
        accept(input, value);
        if (listCompletedParameters().containsAll(listInputs())) {
            run();
        }
    }

    Set<Input<?>> listInputs();

    Set<Input<?>> listCompletedParameters();

}
