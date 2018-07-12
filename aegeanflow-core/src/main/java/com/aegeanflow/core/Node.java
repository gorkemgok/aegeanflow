package com.aegeanflow.core;

import java.util.Collection;
import java.util.Optional;
import java.util.UUID;

public interface Node {

    void initialize(UUID uuid, Router router);

    void run();

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

    UUID getUUID();

    NodeId getId();

}
