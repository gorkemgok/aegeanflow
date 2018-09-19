package com.aegeanflow.core.table;

import java.util.Iterator;
import java.util.concurrent.BlockingQueue;

public class TableListIterator implements Iterator<Row> {

    private final BlockingQueue<Row> queue;

    private Row next;

    protected TableListIterator(BlockingQueue<Row> queue) {
        this.queue = queue;
    }

    @Override
    public boolean hasNext() {
        try {
            next = queue.take();
        } catch (InterruptedException e) {
            return false;
        }
        return true;
    }

    @Override
    public Row next() {
        return next;
    }
}
