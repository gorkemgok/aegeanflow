package com.aegeanflow.core;

import java.util.*;
import java.util.stream.Collectors;

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

    public  <T> void acceptAndRun(Input<T> input, T value) {
        accept(input, value);
        executeIfSatisfied();
    }

    @Override
    public void executeIfSatisfied() {
        if (isSatisfied()) {
            execute();
        }
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

    protected abstract <T> void setInput(Input<T> input, T value);
}
