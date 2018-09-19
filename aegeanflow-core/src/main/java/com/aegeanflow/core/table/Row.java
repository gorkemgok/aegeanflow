package com.aegeanflow.core.table;

public class Row {

    private final Object[] values;

    public Row(Object... values) {
        this.values = values;
    }

    public Object get(int i) {
        return values[i];
    }
}
