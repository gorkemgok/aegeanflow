package com.aegeanflow.essentials.data;

import com.aegeanflow.core.Exchange;

public class TabularDataExchange implements Exchange<TabularData> {

    private final TabularData tabularData;

    public TabularDataExchange(TabularData tabularData) {
        this.tabularData = tabularData;
    }

    @Override
    public TabularData get() {
        return tabularData;
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
