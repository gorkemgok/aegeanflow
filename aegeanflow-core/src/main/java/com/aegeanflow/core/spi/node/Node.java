package com.aegeanflow.core.spi.node;

import com.aegeanflow.core.exception.IllegalNodeConfigurationException;
import com.aegeanflow.core.spi.parameter.Input;
import com.aegeanflow.core.spi.parameter.Output;
import com.aegeanflow.core.route.Router;

import java.util.Collection;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

public interface Node extends InputAcceptor {

    enum State {
        WAITING, RUNNING, CANCELED, FAILED, DONE
    }

    UUID getId();

    String getName();

    void initialize(UUID uuid, Router router);

    void execute() throws Exception;

    State getState();

    void reset();

    void clear();

    boolean isReady();

    Collection<Output<?>> getOutputs();

    Collection<Input<?>> getInputs();

    Collection<Input<?>> getCompletedParameters();

    default Optional<Output<?>> getOutput(String name) {
        Optional<Output<?>> outputOptional =  getOutputs().stream()
            .filter(output -> output.name().equals(name))
            .findAny();
        return outputOptional;
    }

    default Optional<Input<?>> getInput(String name) {
        Optional<Input<?>> inputOptional =  getInputs().stream()
            .filter(input -> input.name().equals(name))
            .findAny();
        return inputOptional;
    }

    default boolean is(UUID uuid) {
        return getId().equals(uuid);
    }

    default Collection<String> getInputNames() {
        return getInputs().stream().map(Input::name).collect(Collectors.toList());
    }

    default Optional<Class> getInputType(String name) {
        Optional<Class> classOptional =  getInputs().stream()
            .filter(input -> input.name().equals(name))
            .map(input -> (Class)input.type())
            .findAny();
        return classOptional;
    }

    default Collection<String> getOutputNames() {
        return getOutputs().stream().map(Output::name).collect(Collectors.toList());
    }

    default Optional<Class> getOutputType(String name) {
        Optional<Class> classOptional =  getOutputs().stream()
            .filter(output -> output.name().equals(name))
            .map(output -> (Class)output.type())
            .findAny();
        return classOptional;
    }

}
