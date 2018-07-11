package com.aegeanflow.essentials.data;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Created by gorkem on 12.01.2018.
 */
public class TabularData {

    public static class Builder{

        private List<Field> fieldList;

        private List<List<Object>> data;

        public Builder addField(String name, Class<?> type){
            fieldList.add(new Field(name, type));
            return this;
        }

        public Builder addRow(Object... values){
            data.add(Arrays.asList(values));
            return this;
        }

        public Builder addRow(List<Object> row){
            data.add(row);
            return this;
        }

        public TabularData build(){
            return new TabularData(new Schema(fieldList), data);
        }
    }

    public static class Schema{
        private final List<Field> fieldList;

        public Schema(List<Field> fieldList) {
            this.fieldList = fieldList;
        }

        public List<Field> getFieldList() {
            return Collections.unmodifiableList(fieldList);
        }
    }

    public static class Field{
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

    private final Schema schema;

    private final List<List<Object>> data;

    public TabularData(Schema schema, List<List<Object>> data) {
        this.schema = schema;
        this.data = data;
    }

    public Schema getSchema() {
        return schema;
    }

    public List<List<Object>> getData() {
        return data;
    }
}
