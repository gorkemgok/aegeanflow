package com.aegeanflow.core.exchange;

public interface Exchange<T> {

    static <T> Exchange<T> createNonpersistent(T value) {
        return new NonpersistentExchange<>(value);
    }

    static Exchange<String> of (String value) {
        return new StringExchange(value);
    }

    static Exchange<Double> of (Double value) {
        return new DoubleExchange(value);
    }

    T get();

    long size();

    boolean isPersistable();

}
