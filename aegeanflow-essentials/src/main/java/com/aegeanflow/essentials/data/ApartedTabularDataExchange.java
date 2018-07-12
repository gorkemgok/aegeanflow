package com.aegeanflow.essentials.data;

import com.aegeanflow.core.Exchange;

public class ApartedTabularDataExchange implements Exchange<ApartedTabularData> {

    private final ApartedTabularData apartedTabularData;

    public ApartedTabularDataExchange(ApartedTabularData apartedTabularData) {
        this.apartedTabularData = apartedTabularData;
    }

    @Override
    public ApartedTabularData get() {
        return apartedTabularData;
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
}
