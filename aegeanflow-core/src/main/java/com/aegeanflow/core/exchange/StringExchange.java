package com.aegeanflow.core.exchange;

public final class StringExchange extends AbstractExchange<String> {

    protected StringExchange(String value) {
        super(value);
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

}
