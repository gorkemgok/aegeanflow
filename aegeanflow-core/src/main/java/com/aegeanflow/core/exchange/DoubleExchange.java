package com.aegeanflow.core.exchange;

import java.nio.ByteBuffer;

public class DoubleExchange extends AbstractExchange<Double>{

    protected DoubleExchange(byte[] value) {
        super(value);
    }

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

    @Override
    public byte[] serialize() {
        byte[] bytes = new byte[8];
        ByteBuffer.wrap(bytes).putDouble(value);
        return bytes;
    }

    @Override
    public Double deserialize(byte[] value) {
        return ByteBuffer.wrap(value).getDouble();
    }
}
