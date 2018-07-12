package com.aegeanflow.core;

public interface Exchange<T> {

    static <T> Exchange<T> create(T value) {
        return new Exchange<T>() {
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
                return new byte[0];
            }
        };
    }

    T get();

    long size();

    boolean isPersistable();

    byte[] serialize();

}
