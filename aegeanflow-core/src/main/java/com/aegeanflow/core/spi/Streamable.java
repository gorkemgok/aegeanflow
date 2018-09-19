package com.aegeanflow.core.spi;

public interface Streamable<I> extends Iterable<I>, AutoCloseable{

    void add(I item);

    boolean isFinished();

    @Override
    void close();
}
