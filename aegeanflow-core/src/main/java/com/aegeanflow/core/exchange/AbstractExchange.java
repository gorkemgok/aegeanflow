package com.aegeanflow.core.exchange;

public abstract class AbstractExchange<T> implements Exchange<T>{

    protected final T value;

    public AbstractExchange(byte[] value) {
        this.value = deserialize(value);
    }

    public AbstractExchange(T value) {
        this.value = value;
    }

    @Override
    public T get() {
        return value;
    }

}
