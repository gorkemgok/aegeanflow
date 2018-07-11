package com.aegeanflow.essentials.data;

import java.util.List;

public class SchemalessData {

    private final List<List<Object>> data;

    public SchemalessData(List<List<Object>> data) {
        this.data = data;
    }

    public List<List<Object>> getData() {
        return data;
    }
}
