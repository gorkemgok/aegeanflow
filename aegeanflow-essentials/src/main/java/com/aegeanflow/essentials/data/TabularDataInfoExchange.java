package com.aegeanflow.essentials.data;

import com.aegeanflow.core.Exchange;

public class TabularDataInfoExchange implements Exchange<TabularDataInfo>{

    private final TabularDataInfo tabularDataInfo;

    public TabularDataInfoExchange(TabularDataInfo tabularDataInfo) {
        this.tabularDataInfo = tabularDataInfo;
    }

    @Override
    public TabularDataInfo get() {
        return tabularDataInfo;
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
