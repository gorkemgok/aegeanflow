package com.aegeanflow.core.engine;

import java.util.UUID;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * Created by gorkem on 12.01.2018.
 */
public class OutputFuture<T> implements Future<T> {

    private final DataFlowEngine.IOPair ioPair;

    private final FlowFuture<T> future;

    public OutputFuture(DataFlowEngine.IOPair ioPair, FlowFuture<T> future) {
        this.ioPair = ioPair;
        this.future = future;
    }

    public DataFlowEngine.IOPair getIoPair() {
        return ioPair;
    }

    public UUID getUUID(){
        return future.getUuid();
    }

    @Override
    public boolean cancel(boolean mayInterruptIfRunning) {
        return future.cancel(mayInterruptIfRunning);
    }

    @Override
    public boolean isCancelled() {
        return future.isCancelled();
    }

    @Override
    public boolean isDone() {
        return future.isDone();
    }

    @Override
    public T get() throws InterruptedException, ExecutionException {
        return future.get();
    }

    @Override
    public T get(long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
        return future.get(timeout, unit);
    }
}
