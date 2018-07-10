package com.aegeanflow.core;

public interface Parameter<T> {

    String name();

    Class<T> type();

    static <T>  Parameter<T> of(String name, Class<T> type) {
        return new ParameterImpl(name, type);
    }
}
