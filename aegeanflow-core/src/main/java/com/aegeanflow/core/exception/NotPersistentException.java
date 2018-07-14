package com.aegeanflow.core.exception;

public class NotPersistentException extends RuntimeException{

    private final Class<?> clazz;

    public NotPersistentException(Class<?> clazz) {
        this.clazz = clazz;
    }

    public Class<?> getClazz() {
        return clazz;
    }
}
