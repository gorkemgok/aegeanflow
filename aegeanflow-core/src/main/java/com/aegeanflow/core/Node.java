package com.aegeanflow.core;

import java.util.Collection;
import java.util.Optional;
import java.util.UUID;

public interface Node {

    default boolean is(UUID uuid) {
        return getUUID().equals(uuid);
    }

    UUID getUUID();

    String getName();

    NodeId getId();

    void initialize(UUID uuid, Router router);

    void execute();

    void executeIfSatisfied();

    boolean isSatisfied();

    <T> void accept(Input<T> input, T value);

    <T> void acceptAndRun(Input<T> input, T value);

    Collection<String> getInputNames();

    Optional<Class> getInputType(String name);

    Collection<String> getOutputNames();

    Optional<Class> getOutputType(String name);

    Collection<Output<?>> getOutputs();

    Collection<Input<?>> getInputs();

    Optional<Output<?>> getOutput(String name);

    Optional<Input<?>> getInput(String name);

    Collection<Input<?>> getCompletedParameters();

}
