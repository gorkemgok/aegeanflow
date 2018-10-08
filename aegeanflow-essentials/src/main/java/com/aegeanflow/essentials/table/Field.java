package com.aegeanflow.essentials.table;

public class Field {
    private final String name;

    private final Class<?> type;

    public Field(String name, Class<?> type) {
        this.name = name;
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public Class<?> getType() {
        return type;
    }
}
