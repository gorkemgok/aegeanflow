package com.aegeanflow.core;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public abstract class AbstractNode implements Node{

    protected NodeRouter router;

    private UUID uuid;

    private Set<Input<?>> completedParameterNames = new HashSet<>();

    @Override
    public UUID getUUID() {
        return uuid;
    }

    @Override
    public void initialize(UUID uuid, Router router) {
        this.uuid = uuid;
        this.router = new NodeRouter(this, router);
    }

    @Override
    public <T> void accept(Input<T> input, T value) {
        setInput(input, value);
        completedParameterNames.add(input);
    }

    protected abstract <T> void setInput(Input<T> input, T value);

    @Override
    public Set<Input<?>> listCompletedParameters() {
        return completedParameterNames;
    }
}
