package com.aegeanflow.essentials.table;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Collections;
import java.util.List;

public class Schema {

    private final List<Field> fieldList;

    @JsonCreator
    public Schema(
            @JsonProperty("fieldList") List<Field> fieldList) {
        this.fieldList = fieldList;
    }

    public List<Field> getFieldList() {
        return Collections.unmodifiableList(fieldList);
    }

    @JsonIgnore
    public int getFieldIndex(String fieldName) {
        for (int i = 0; i < fieldList.size(); i++) {
            Field field = fieldList.get(i);
            if (field.getName().equals(fieldName)) {
                return i;
            }
        }
        return -1;
    }
}
