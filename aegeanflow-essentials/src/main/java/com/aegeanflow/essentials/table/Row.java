package com.aegeanflow.essentials.table;

import java.util.Arrays;

public class Row {

    private final Object[] values;

    public Row(Object... values) {
        this.values = values;
    }

    public Object get(int i) {
        return values[i];
    }

    public int size() {
        return  values.length;
    }

    @Override
    public String toString() {
        return "Row{" +
            "values=" + Arrays.toString(values) +
            '}';
    }
}
