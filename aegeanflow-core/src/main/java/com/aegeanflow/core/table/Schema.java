package com.aegeanflow.core.table;

import java.util.Collections;
import java.util.List;

public class Schema {
    private final List<Field> fieldList;

    public Schema(List<Field> fieldList) {
        this.fieldList = fieldList;
    }

    public List<Field> getFieldList() {
        return Collections.unmodifiableList(fieldList);
    }
}
