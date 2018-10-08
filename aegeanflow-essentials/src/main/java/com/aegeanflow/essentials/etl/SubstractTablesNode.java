package com.aegeanflow.essentials.etl;

import com.aegeanflow.core.route.tunnel.StreamTunnel;
import com.aegeanflow.core.spi.node.AbstractSynchronizedNode;
import com.aegeanflow.core.spi.parameter.Input;
import com.aegeanflow.core.spi.parameter.Output;
import com.aegeanflow.core.spi.parameter.Parameter;
import com.aegeanflow.essentials.table.RandomAccessTable;
import com.aegeanflow.essentials.table.Row;
import com.aegeanflow.essentials.table.Table;
import com.aegeanflow.essentials.util.RowUtil;

import java.util.*;

public class SubstractTablesNode extends AbstractSynchronizedNode {

    public static final Input<Boolean> ONE_MINUS_TWO = Parameter.input("one_minus_two", Boolean.class);
    public static final Input<Table> TABLE_INPUT_1 = Parameter.input("table_input_1", Table.class);
    public static final Input<Table> TABLE_INPUT_2 = Parameter.input("table_input_2", Table.class);
    public static final Output<Table> DIFFERENCE_TABLE_OUTPUT = Parameter.output("difference_table_output", Table.class);

    private Boolean oneMinusTwo = true;
    private Table table1;
    private Table table2;

    @Override
    protected void run() throws Exception {
        Table one = !oneMinusTwo ? table1 : table2;
        Table two = !oneMinusTwo ? table2 : table1;
        Collection<Row> oneList = one.collect();
        Iterator<Row> twoIterator = two.iterator();
        Table differenceTable = new RandomAccessTable(one.getSchema());
        try (StreamTunnel<Row> stream = router.next(DIFFERENCE_TABLE_OUTPUT, differenceTable)){
            while (twoIterator.hasNext()) {
                Row twoRow = twoIterator.next();
                boolean exists = oneList.stream().anyMatch(oneRow -> RowUtil.equals(oneRow, twoRow, 0.01));
                if(!exists) {
                    stream.send(twoRow);
                }
            }
        }
    }

    @Override
    public boolean isReady() {
        return table1 != null && table2 != null && oneMinusTwo != null;
    }

    @Override
    protected <T> void setInput(Input<T> input, T value) {
        if (input.equals(TABLE_INPUT_1)) {
            table1 = (Table) value;
        }

        if (input.equals(TABLE_INPUT_2)) {
            table2 = (Table) value;
        }

        if (input.equals(ONE_MINUS_TWO)) {
            oneMinusTwo = (Boolean) value;
        }
    }

    @Override
    public String getName() {
        return "Subtract Table";
    }

    @Override
    public Collection<Output<?>> getOutputs() {
        return Arrays.asList(DIFFERENCE_TABLE_OUTPUT);
    }

    @Override
    public Collection<Input<?>> getInputs() {
        return Arrays.asList(TABLE_INPUT_1, TABLE_INPUT_2);
    }
}
