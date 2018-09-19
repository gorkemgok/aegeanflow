package com.aegeanflow.core.concurrent;

import com.aegeanflow.core.session.Session;
import com.aegeanflow.core.spi.node.Node;

import java.util.concurrent.*;
import java.util.function.*;

public class NodeFuture<T> extends CompletableFuture<T> {

    private final Session session;

    private final Node node;

    private final CompletableFuture<T> completableFuture;

    public NodeFuture(Session session, Node node, CompletableFuture<T> completableFuture) {
        this.session = session;
        this.node = node;
        this.completableFuture = completableFuture;
    }

    public Session getSession() {
        return session;
    }

    public Node getNode() {
        return node;
    }

    public static <U> CompletableFuture<U> supplyAsync(Supplier<U> supplier) {
        return CompletableFuture.supplyAsync(supplier);
    }

    public static <U> CompletableFuture<U> supplyAsync(Supplier<U> supplier, Executor executor) {
        return CompletableFuture.supplyAsync(supplier, executor);
    }

    public static CompletableFuture<Void> runAsync(Runnable runnable) {
        return CompletableFuture.runAsync(runnable);
    }

    public static CompletableFuture<Void> runAsync(Runnable runnable, Executor executor) {
        return CompletableFuture.runAsync(runnable, executor);
    }

    public static <U> CompletableFuture<U> completedFuture(U value) {
        return CompletableFuture.completedFuture(value);
    }

    @Override
    public boolean isDone() {
        return completableFuture.isDone();
    }

    @Override
    public T get() throws InterruptedException, ExecutionException {
        return completableFuture.get();
    }

    @Override
    public T get(long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
        return completableFuture.get(timeout, unit);
    }

    @Override
    public T join() {
        return completableFuture.join();
    }

    @Override
    public T getNow(T valueIfAbsent) {
        return completableFuture.getNow(valueIfAbsent);
    }

    @Override
    public boolean complete(T value) {
        return completableFuture.complete(value);
    }

    @Override
    public boolean completeExceptionally(Throwable ex) {
        return completableFuture.completeExceptionally(ex);
    }

    @Override
    public <U> CompletableFuture<U> thenApply(Function<? super T, ? extends U> fn) {
        return completableFuture.thenApply(fn);
    }

    @Override
    public <U> CompletableFuture<U> thenApplyAsync(Function<? super T, ? extends U> fn) {
        return completableFuture.thenApplyAsync(fn);
    }

    @Override
    public <U> CompletableFuture<U> thenApplyAsync(Function<? super T, ? extends U> fn, Executor executor) {
        return completableFuture.thenApplyAsync(fn, executor);
    }

    @Override
    public CompletableFuture<Void> thenAccept(Consumer<? super T> action) {
        return completableFuture.thenAccept(action);
    }

    @Override
    public CompletableFuture<Void> thenAcceptAsync(Consumer<? super T> action) {
        return completableFuture.thenAcceptAsync(action);
    }

    @Override
    public CompletableFuture<Void> thenAcceptAsync(Consumer<? super T> action, Executor executor) {
        return completableFuture.thenAcceptAsync(action, executor);
    }

    @Override
    public CompletableFuture<Void> thenRun(Runnable action) {
        return completableFuture.thenRun(action);
    }

    @Override
    public CompletableFuture<Void> thenRunAsync(Runnable action) {
        return completableFuture.thenRunAsync(action);
    }

    @Override
    public CompletableFuture<Void> thenRunAsync(Runnable action, Executor executor) {
        return completableFuture.thenRunAsync(action, executor);
    }

    @Override
    public <U, V> CompletableFuture<V> thenCombine(CompletionStage<? extends U> other, BiFunction<? super T, ? super U, ? extends V> fn) {
        return completableFuture.thenCombine(other, fn);
    }

    @Override
    public <U, V> CompletableFuture<V> thenCombineAsync(CompletionStage<? extends U> other, BiFunction<? super T, ? super U, ? extends V> fn) {
        return completableFuture.thenCombineAsync(other, fn);
    }

    @Override
    public <U, V> CompletableFuture<V> thenCombineAsync(CompletionStage<? extends U> other, BiFunction<? super T, ? super U, ? extends V> fn, Executor executor) {
        return completableFuture.thenCombineAsync(other, fn, executor);
    }

    @Override
    public <U> CompletableFuture<Void> thenAcceptBoth(CompletionStage<? extends U> other, BiConsumer<? super T, ? super U> action) {
        return completableFuture.thenAcceptBoth(other, action);
    }

    @Override
    public <U> CompletableFuture<Void> thenAcceptBothAsync(CompletionStage<? extends U> other, BiConsumer<? super T, ? super U> action) {
        return completableFuture.thenAcceptBothAsync(other, action);
    }

    @Override
    public <U> CompletableFuture<Void> thenAcceptBothAsync(CompletionStage<? extends U> other, BiConsumer<? super T, ? super U> action, Executor executor) {
        return completableFuture.thenAcceptBothAsync(other, action, executor);
    }

    @Override
    public CompletableFuture<Void> runAfterBoth(CompletionStage<?> other, Runnable action) {
        return completableFuture.runAfterBoth(other, action);
    }

    @Override
    public CompletableFuture<Void> runAfterBothAsync(CompletionStage<?> other, Runnable action) {
        return completableFuture.runAfterBothAsync(other, action);
    }

    @Override
    public CompletableFuture<Void> runAfterBothAsync(CompletionStage<?> other, Runnable action, Executor executor) {
        return completableFuture.runAfterBothAsync(other, action, executor);
    }

    @Override
    public <U> CompletableFuture<U> applyToEither(CompletionStage<? extends T> other, Function<? super T, U> fn) {
        return completableFuture.applyToEither(other, fn);
    }

    @Override
    public <U> CompletableFuture<U> applyToEitherAsync(CompletionStage<? extends T> other, Function<? super T, U> fn) {
        return completableFuture.applyToEitherAsync(other, fn);
    }

    @Override
    public <U> CompletableFuture<U> applyToEitherAsync(CompletionStage<? extends T> other, Function<? super T, U> fn, Executor executor) {
        return completableFuture.applyToEitherAsync(other, fn, executor);
    }

    @Override
    public CompletableFuture<Void> acceptEither(CompletionStage<? extends T> other, Consumer<? super T> action) {
        return completableFuture.acceptEither(other, action);
    }

    @Override
    public CompletableFuture<Void> acceptEitherAsync(CompletionStage<? extends T> other, Consumer<? super T> action) {
        return completableFuture.acceptEitherAsync(other, action);
    }

    @Override
    public CompletableFuture<Void> acceptEitherAsync(CompletionStage<? extends T> other, Consumer<? super T> action, Executor executor) {
        return completableFuture.acceptEitherAsync(other, action, executor);
    }

    @Override
    public CompletableFuture<Void> runAfterEither(CompletionStage<?> other, Runnable action) {
        return completableFuture.runAfterEither(other, action);
    }

    @Override
    public CompletableFuture<Void> runAfterEitherAsync(CompletionStage<?> other, Runnable action) {
        return completableFuture.runAfterEitherAsync(other, action);
    }

    @Override
    public CompletableFuture<Void> runAfterEitherAsync(CompletionStage<?> other, Runnable action, Executor executor) {
        return completableFuture.runAfterEitherAsync(other, action, executor);
    }

    @Override
    public <U> CompletableFuture<U> thenCompose(Function<? super T, ? extends CompletionStage<U>> fn) {
        return completableFuture.thenCompose(fn);
    }

    @Override
    public <U> CompletableFuture<U> thenComposeAsync(Function<? super T, ? extends CompletionStage<U>> fn) {
        return completableFuture.thenComposeAsync(fn);
    }

    @Override
    public <U> CompletableFuture<U> thenComposeAsync(Function<? super T, ? extends CompletionStage<U>> fn, Executor executor) {
        return completableFuture.thenComposeAsync(fn, executor);
    }

    @Override
    public CompletableFuture<T> whenComplete(BiConsumer<? super T, ? super Throwable> action) {
        return completableFuture.whenComplete(action);
    }

    @Override
    public CompletableFuture<T> whenCompleteAsync(BiConsumer<? super T, ? super Throwable> action) {
        return completableFuture.whenCompleteAsync(action);
    }

    @Override
    public CompletableFuture<T> whenCompleteAsync(BiConsumer<? super T, ? super Throwable> action, Executor executor) {
        return completableFuture.whenCompleteAsync(action, executor);
    }

    @Override
    public <U> CompletableFuture<U> handle(BiFunction<? super T, Throwable, ? extends U> fn) {
        return completableFuture.handle(fn);
    }

    @Override
    public <U> CompletableFuture<U> handleAsync(BiFunction<? super T, Throwable, ? extends U> fn) {
        return completableFuture.handleAsync(fn);
    }

    @Override
    public <U> CompletableFuture<U> handleAsync(BiFunction<? super T, Throwable, ? extends U> fn, Executor executor) {
        return completableFuture.handleAsync(fn, executor);
    }

    @Override
    public CompletableFuture<T> toCompletableFuture() {
        return completableFuture.toCompletableFuture();
    }

    @Override
    public CompletableFuture<T> exceptionally(Function<Throwable, ? extends T> fn) {
        return completableFuture.exceptionally(fn);
    }

    public static CompletableFuture<Void> allOf(CompletableFuture<?>... cfs) {
        return CompletableFuture.allOf(cfs);
    }

    public static CompletableFuture<Object> anyOf(CompletableFuture<?>... cfs) {
        return CompletableFuture.anyOf(cfs);
    }

    @Override
    public boolean cancel(boolean mayInterruptIfRunning) {
        return completableFuture.cancel(mayInterruptIfRunning);
    }

    @Override
    public boolean isCancelled() {
        return completableFuture.isCancelled();
    }

    @Override
    public boolean isCompletedExceptionally() {
        return completableFuture.isCompletedExceptionally();
    }

    @Override
    public void obtrudeValue(T value) {
        completableFuture.obtrudeValue(value);
    }

    @Override
    public void obtrudeException(Throwable ex) {
        completableFuture.obtrudeException(ex);
    }

    @Override
    public int getNumberOfDependents() {
        return completableFuture.getNumberOfDependents();
    }

    @Override
    public String toString() {
        return completableFuture.toString();
    }
}
