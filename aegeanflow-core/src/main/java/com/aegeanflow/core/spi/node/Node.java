package com.aegeanflow.core.spi.node;

import com.aegeanflow.core.spi.parameter.Input;
import com.aegeanflow.core.spi.parameter.Output;
import com.aegeanflow.core.route.Router;

import java.util.Collection;
import java.util.Optional;
import java.util.UUID;

public interface Node {

    enum State {
        WAITING, RUNNING, CANCELED, FAILED, DONE
    }

    default boolean is(UUID uuid) {
        return getUUID().equals(uuid);
    }

    UUID getUUID();

    String getName();

    void initialize(UUID uuid, Router router);

    void execute();

    State getState();

    <T> void accept(Input<T> input, T value);

    void reset();

    void clear();

    boolean isSatisfied();

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
