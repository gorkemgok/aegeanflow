package com.aegeanflow.core.spi.parameter;

import com.google.inject.TypeLiteral;

public interface Parameter<T> {

    String name();

    Class<T> type();

    TypeLiteral<T> typeLiteral();

    static <T>  Parameter<T> of(String name, Class<T> type) {
        return new ParameterImpl(name, type);
    }

    static <T> Output<T> output(String name, Class<T> type) {
        return new Output<>(name, type);
    }

    static <T> Output<T> output(String name, TypeLiteral<T> typeLiteral) {
        return new Output<>(name, typeLiteral);
    }

    static <T> Input<T> input(String name, Class<T> type) {
        return new Input<>(name, type);
    }

    static <T> Input<T> input(String name, TypeLiteral<T> typeLiteral) {
        return new Input<>(name, typeLiteral);
    }

    static Input<String> stringInput(String name) {
        return input(name, String.class);
    }

    static Output<String> stringOutput(String name) {
        return output(name, String.class);
    }
}
