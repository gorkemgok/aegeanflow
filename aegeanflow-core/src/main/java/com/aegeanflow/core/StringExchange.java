package com.aegeanflow.core;

public final class StringExchange implements Exchange<String>{

    private final String value;

    public StringExchange(byte[] value) {
        this.value = new String(value);
    }

    public StringExchange(String value) {
        this.value = value;
    }

    @Override
    public String get() {
        return value;
    }

    @Override
    public long size() {
        return value.getBytes().length;
    }

    @Override
    public boolean isPersistable() {
        return true;
    }

    @Override
    public byte[] serialize() {
        return value.getBytes();
    }

}
