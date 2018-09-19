package com.aegeanflow.core.spi.node;

import com.aegeanflow.core.route.NodeRouter;
import com.aegeanflow.core.route.Router;
import com.aegeanflow.core.spi.parameter.Input;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public abstract class AbstractNode implements Node {

    protected State state;

    protected NodeRouter router;

    private UUID uuid;

    private Set<Input<?>> completedParameterNames = new HashSet<>();

    private final static ReentrantReadWriteLock rwLock = new ReentrantReadWriteLock();

    public AbstractNode() {
        state = State.WAITING;
    }

    protected abstract void run();

    protected abstract <T> void setInput(Input<T> input, T value);

    public void setState(State state) {
        try {
            rwLock.writeLock().lock();
            this.state = state;
        } finally {
            rwLock.writeLock().unlock();
        }
    }

    @Override
    public void execute() {
        try {
            run();
        } catch (Exception e) {
            setState(State.FAILED);
            throw e;
        }
        setState(State.DONE);
    }

    @Override
    public State getState() {
        try {
            rwLock.readLock().lock();
            return state;
        } finally {
            rwLock.readLock().unlock();
        }
    }

    @Override
    public void initialize(UUID uuid, Router router) {
        this.uuid = uuid;
        this.router = new NodeRouter(this, router);
    }

    @Override
    public void reset() {
        setState(State.WAITING);
    }

    @Override
    public void clear() {
        reset();
        getInputs().forEach(input -> setInput(input, null));
    }

    @Override
    public UUID getUUID() {
        return uuid;
    }

    @Override
    public boolean isReady() {
        return getCompletedParameters().containsAll(getInputs());
    }

    @Override
    public Set<Input<?>> getCompletedParameters() {
        return completedParameterNames;
    }

}