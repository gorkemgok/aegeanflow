package com.aegeanflow.core.exchange;

public class DoubleExchange extends AbstractExchange<Double>{

    protected DoubleExchange(Double value) {
        super(value);
    }

    @Override
    public long size() {
        return Double.BYTES;
    }

    @Override
    public boolean isPersistable() {
        return true;
    }
}
