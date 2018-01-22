package com.aegeanflow.core.engine;

/**
 * Created by gorkem on 12.01.2018.
 */
public class FlowInput<T> {

    private final String name;

    private final T value;

    public FlowInput(String name, T value) {
        this.name = name;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public T getValue() {
        return value;
    }
}
