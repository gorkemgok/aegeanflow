package com.aegeanflow.core;

import java.util.Set;

public interface Node {

    void initialize(Router router);

    void run();

    <T> void accept(Parameter<T> input, T value);

    default <T> void acceptAndRun(Parameter<T> input, T value) {
        accept(input, value);
        if (listCompletedParameters().containsAll(listParameters())) {
            run();
        }
    }

    Set<Parameter> listParameters();

    Set<Parameter> listCompletedParameters();

}
