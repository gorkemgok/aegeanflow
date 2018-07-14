package com.aegeanflow.core.spi.node;

import com.aegeanflow.core.spi.node.Node;
import com.aegeanflow.core.spi.parameter.Input;
import com.aegeanflow.core.spi.parameter.Output;
import com.aegeanflow.core.route.NodeRouter;
import com.aegeanflow.core.route.Router;

import java.util.*;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.stream.Collectors;

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
    public boolean isSatisfied() {
        return getCompletedParameters().containsAll(getInputs());
    }

    @Override
    public Set<Input<?>> getCompletedParameters() {
        return completedParameterNames;
    }

    @Override
    public Collection<String> getInputNames() {
        return getInputs().stream().map(Input::name).collect(Collectors.toList());
    }

    @Override
    public Collection<String> getOutputNames() {
        return getOutputs().stream().map(Output::name).collect(Collectors.toList());
    }

    @Override
    public Optional<Class> getInputType(String name) {
        Optional<Class> classOptional =  getInputs().stream()
            .filter(input -> input.name().equals(name))
            .map(input -> (Class)input.type())
            .findAny();
        return classOptional;
    }

    @Override
    public Optional<Class> getOutputType(String name) {
        Optional<Class> classOptional =  getOutputs().stream()
            .filter(output -> output.name().equals(name))
            .map(output -> (Class)output.type())
            .findAny();
        return classOptional;
    }

    @Override
    public Optional<Output<?>> getOutput(String name) {
        Optional<Output<?>> outputOptional =  getOutputs().stream()
            .filter(output -> output.name().equals(name))
            .findAny();
        return outputOptional;
    }

    @Override
    public Optional<Input<?>> getInput(String name) {
        Optional<Input<?>> inputOptional =  getInputs().stream()
            .filter(input -> input.name().equals(name))
            .findAny();
        return inputOptional;
    }
}