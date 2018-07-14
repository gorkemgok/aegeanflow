package com.aegeanflow.core.box.definition;

import com.aegeanflow.core.spi.box.AnnotatedBox;

import java.util.List;

public class BoxDefinition {

    private final Class<? extends AnnotatedBox> type;

    private final String name;

    private final List<BoxIODefinition> inputs;

    private final List<BoxIODefinition> outputs;

    public BoxDefinition(Class<? extends AnnotatedBox> type, String name, List<BoxIODefinition> inputs, List<BoxIODefinition> outputs) {
        this.type = type;
        this.name = name;
        this.inputs = inputs;
        this.outputs = outputs;
    }


    public Class<? extends AnnotatedBox> getType() {
        return type;
    }

    public String getName() {
        return name;
    }

    public List<BoxIODefinition> getInputs() {
        return inputs;
    }

    public List<BoxIODefinition> getOutputs() {
        return outputs;
    }
}
