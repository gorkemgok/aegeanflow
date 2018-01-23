package com.aegeanflow.core.engine;

import java.util.UUID;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * Created by gorkem on 12.01.2018.
 */
public class FlowFuture<T> implements Future<T> {

    private final UUID uuid;

    private final Future<T> future;

    public FlowFuture(UUID uuid, Future<T> future) {
        this.uuid = uuid;
        this.future = future;
    }

    public UUID getUuid() {
        return uuid;
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
