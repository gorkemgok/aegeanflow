package com.aegeanflow.core.spi;

import java.util.Collection;

public interface Streamable<I> extends Iterable<I>, AutoCloseable{

    void add(I item);

    boolean isFinished();

    Collection<I> collect() throws InterruptedException;

    @Override
    void close();
}
