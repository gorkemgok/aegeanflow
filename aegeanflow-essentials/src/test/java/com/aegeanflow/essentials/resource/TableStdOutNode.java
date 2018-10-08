package com.aegeanflow.essentials.resource;

import com.aegeanflow.core.spi.node.AbstractSynchronizedNode;
import com.aegeanflow.core.spi.parameter.Input;
import com.aegeanflow.core.spi.parameter.Output;
import com.aegeanflow.core.spi.parameter.Parameter;
import com.aegeanflow.essentials.table.Row;
import com.aegeanflow.essentials.table.Table;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

public class TableStdOutNode extends AbstractSynchronizedNode {

    public final static Input<Table> TABLE_INPUT = Parameter.input("table_input", Table.class);

    private Table table;

    @Override
    protected void run() {
        if (table != null) {
            for (Row row : table) {
                System.out.println(row);
            }
        }
    }

    @Override
    protected <T> void setInput(Input<T> input, T value) {
        if (TABLE_INPUT.equals(input)) {
            table = (Table) value;
        }
    }

    @Override
    public String getName() {
        return "TableStdOut";
    }

    @Override
    public Collection<Output<?>> getOutputs() {
        return Collections.EMPTY_LIST;
    }

    @Override
    public Collection<Input<?>> getInputs() {
        return Arrays.asList(TABLE_INPUT);
    }

}
