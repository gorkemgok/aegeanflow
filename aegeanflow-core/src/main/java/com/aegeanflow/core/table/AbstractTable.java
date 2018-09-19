package com.aegeanflow.core.table;

public abstract class AbstractTable implements Table {

    private final Schema schema;

    protected AbstractTable(Schema schema) {
        this.schema = schema;
    }

    @Override
    public Schema getSchema() {
        return schema;
    }
}
