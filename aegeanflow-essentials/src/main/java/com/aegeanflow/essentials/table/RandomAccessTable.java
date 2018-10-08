package com.aegeanflow.essentials.table;

import com.aegeanflow.core.exchange.Exchange;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class RandomAccessTable extends AbstractTable{

    private final TableQueueIterator iterator;

    private final BlockingQueue<Row> queue;

    private final List<Row> data;

    private boolean isFinished = false;

    private final List<TableListIterator> iteratorList;

    public RandomAccessTable(Schema schema){
        super(schema);
        this.queue = new LinkedBlockingQueue<>();
        this.data = new ArrayList<>();
        this.iterator = new TableQueueIterator(queue);
        this.iteratorList = new ArrayList<>();
    }

    @Override
    public Iterator<Row> iterator() {
        TableListIterator iterator = new TableListIterator(data, this);
        iteratorList.add(iterator);
        return iterator;
    }

    @Override
    public Exchange<Table> exchange() {
        return Exchange.createNonpersistent(this);
    }

    @Override
    public void add(Row item) {
        data.add(item);
        for(TableListIterator iterator : iteratorList) {
            synchronized (iterator) {
                iterator.notifyAll();
            }
        }
        queue.offer(item);
    }

    @Override
    public boolean isFinished() {
        return isFinished;
    }

    @Override
    public Collection<Row> collect() throws InterruptedException {
        if (!isFinished) {
            synchronized (this) {
                wait();
            }
        }
        return data;
    }

    @Override
    public void close() {
        isFinished = true;
        for(TableListIterator iterator : iteratorList) {
            synchronized (iterator) {
                iterator.notifyAll();
            }
        }
        synchronized (this) {
            notifyAll();
        }
    }
}
