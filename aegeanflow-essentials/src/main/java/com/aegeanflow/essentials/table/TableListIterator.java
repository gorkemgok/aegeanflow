package com.aegeanflow.essentials.table;

import java.util.Iterator;
import java.util.List;

public class TableListIterator implements Iterator<Row> {

    private final List<Row> data;

    private final Table owner;

    private int index = -1;

    protected TableListIterator(List<Row> data, Table owner) {
        this.data = data;
        this.owner = owner;
    }

    @Override
    public boolean hasNext() {
        while (true) {
            if (index < data.size() - 1) {
                index++;
                return true;
            } else if (owner.isFinished()) {
                return false;
            } else {
                synchronized (this) {
                    try {
                        wait(100);
                    } catch (InterruptedException e) {
                        return false;
                    }
                }
            }
        }
    }

    @Override
    public Row next() {
        return data.get(index);
    }

}
