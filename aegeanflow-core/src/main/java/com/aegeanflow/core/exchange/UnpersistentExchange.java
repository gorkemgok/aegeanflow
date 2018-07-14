package com.aegeanflow.core.exchange;

import com.aegeanflow.core.exception.NotPersistentException;

public class UnpersistentExchange<T> implements Exchange<T> {

    private final T value;

    protected UnpersistentExchange(T value) {
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

    @Override
    public byte[] serialize() {
        throw new NotPersistentException(this.getClass());
    }

    @Override
    public T deserialize(byte[] value) {
        throw new NotPersistentException(this.getClass());
    }
}
