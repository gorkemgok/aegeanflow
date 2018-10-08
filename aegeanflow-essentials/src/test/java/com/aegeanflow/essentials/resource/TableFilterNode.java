package com.aegeanflow.essentials.resource;

import com.aegeanflow.core.route.tunnel.StreamTunnel;
import com.aegeanflow.core.spi.node.AbstractSynchronizedNode;
import com.aegeanflow.core.spi.parameter.Input;
import com.aegeanflow.core.spi.parameter.Output;
import com.aegeanflow.core.spi.parameter.Parameter;
import com.aegeanflow.essentials.table.RandomAccessTable;
import com.aegeanflow.essentials.table.Row;
import com.aegeanflow.essentials.table.Table;

import java.util.Arrays;
import java.util.Collection;

public class TableFilterNode extends AbstractSynchronizedNode {

    public final static Input<Table> INPUT_TABLE = Parameter.input("input", Table.class);
    public final static Output<Table> OUTPUT_TABLE = Parameter.output("output", Table.class);

    private Table table;

    @Override
    protected void run() {
        Table outputTable = new RandomAccessTable(null);
        try(StreamTunnel<Row> stream = router.next(OUTPUT_TABLE, outputTable)) {
            for (Row row : table) {
                if (row.get(2) != null && row.get(2).equals("0111069-001")) {
                    stream.send(row);
                }
            }
        }
    }

    @Override
    protected <T> void setInput(Input<T> input, T value) {
        if (input.equals(INPUT_TABLE)) {
            table = (Table) value;
        }
    }

    @Override
    public String getName() {
        return "TableFilter";
    }

    @Override
    public Collection<Output<?>> getOutputs() {
        return Arrays.asList(OUTPUT_TABLE);
    }

    @Override
    public Collection<Input<?>> getInputs() {
        return Arrays.asList(INPUT_TABLE);
    }
}
