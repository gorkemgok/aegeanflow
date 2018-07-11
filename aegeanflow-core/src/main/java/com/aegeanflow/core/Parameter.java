package com.aegeanflow.core;

public interface Parameter<T> {

    String name();

    Class<T> type();

    static <T>  Parameter<T> of(String name, Class<T> type) {
        return new ParameterImpl(name, type);
    }

    static <T>  Output<T> output(String name, Class<T> type) {
        return new Output<>(name, type);
    }

    static <T>  Input<T> input(String name, Class<T> type) {
        return new Input<>(name, type);
    }
}
