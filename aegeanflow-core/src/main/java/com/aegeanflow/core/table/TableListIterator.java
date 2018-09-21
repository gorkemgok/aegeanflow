package com.aegeanflow.core.table;

import java.util.Iterator;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

public class TableListIterator implements Iterator<Row> {

    private final BlockingQueue<Row> queue;

    private Row next;

    private boolean isFinished = false;

    protected TableListIterator(BlockingQueue<Row> queue) {
        this.queue = queue;
    }

    @Override
    public boolean hasNext() {
        try {
            while (!isFinished) {
                next = queue.poll(100, TimeUnit.MILLISECONDS);
                if (next != null) {
                    return true;
                }
            }
            return false;
        } catch (InterruptedException e) {
            return false;
        }
    }

    @Override
    public Row next() {
        return next;
    }

    public void finish() {
        isFinished = true;
    }
}
