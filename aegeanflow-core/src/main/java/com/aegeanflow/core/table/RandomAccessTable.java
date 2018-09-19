package com.aegeanflow.core.table;

import com.aegeanflow.core.exchange.Exchange;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class RandomAccessTable extends AbstractTable{

    private final TableListIterator iterator;

    private final BlockingQueue<Row> queue;

    private final List<Row> data;

    private boolean isFinished = false;

    public RandomAccessTable(Schema schema){
        super(schema);
        this.queue = new LinkedBlockingQueue<>();
        this.data = new ArrayList<>();
        this.iterator = new TableListIterator(queue);
    }

    @Override
    public Iterator<Row> iterator() {
        return iterator;
    }

    @Override
    public Exchange<Table> exchange() {
        return Exchange.createNonpersistent(this);
    }

    @Override
    public void add(Row item) {
        data.add(item);
        queue.offer(item);
    }

    @Override
    public boolean isFinished() {
        return isFinished;
    }

    @Override
    public void close() {
        isFinished = true;
    }
}
