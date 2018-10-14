package com.aegeanflow.essentials.table;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

public class Row {

    private final Object[] values;

    public Row(Object... values) {
        this.values = values;
    }

    public Row(Row... rows) {
        this.values = Stream.of(rows)
                .map(Row::getValues)
                .flatMap(Stream::of)
                .toArray();
    }

    public Row(List<Object> values) {
        this.values = values.toArray();
    }

    public Object get(int i) {
        return values[i];
    }

    public int size() {
        return  values.length;
    }

    private Object[] getValues(){
        return values;
    }

    @Override
    public String toString() {
        return "Row{" +
            "values=" + Arrays.toString(values) +
            '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Row row = (Row) o;
        return Arrays.equals(values, row.values);
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(values);
    }
}
