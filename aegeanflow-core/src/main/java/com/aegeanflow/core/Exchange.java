package com.aegeanflow.core;

public interface Exchange<T> {

    T get();

    long size();

    boolean isPersistable();

    byte[] serialize();

}
