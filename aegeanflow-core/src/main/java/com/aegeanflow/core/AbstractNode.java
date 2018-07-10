package com.aegeanflow.core;

import java.util.HashSet;
import java.util.Set;

public abstract class AbstractNode implements Node{

    protected NodeRouter router;

    private Set<Parameter> completedParameterNames = new HashSet<>();

    @Override
    public void initialize(Router router) {
        this.router = new NodeRouter(this, router);
    }

    @Override
    public <T> void accept(Parameter<T> input, T value) {
        completedParameterNames.add(input);
    }

    @Override
    public Set<Parameter> listCompletedParameters() {
        return completedParameterNames;
    }
}
