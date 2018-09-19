package com.aegeanflow.core.exchange;

import com.aegeanflow.core.exception.NotPersistentException;

public class NonpersistentExchange<T> implements Exchange<T> {

    private final T value;

    protected NonpersistentExchange(T value) {
        this.value = value;
    }

    @Override
    public T get() {
        return value;
    }

    @Override
    public long size() {
        return 0;
    }

    @Override
    public boolean isPersistable() {
        return false;
    }
}
