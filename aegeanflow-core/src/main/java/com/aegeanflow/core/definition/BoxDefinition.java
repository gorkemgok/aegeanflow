package com.aegeanflow.core.definition;

import com.aegeanflow.core.spi.AnnotatedBox;

import java.util.List;

public class BoxDefinition {

    private Class<? extends AnnotatedBox> type;

    private String label;

    private List<BoxIODefinition> inputs;

    private List<BoxIODefinition> outputs;

    private List<BoxIODefinition> configurations;

    public Class<? extends AnnotatedBox> getType() {
        return type;
    }

    public void setType(Class<? extends AnnotatedBox> type) {
        this.type = type;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public List<BoxIODefinition> getInputs() {
        return inputs;
    }

    public void setInputs(List<BoxIODefinition> inputs) {
        this.inputs = inputs;
    }

    public List<BoxIODefinition> getOutputs() {
        return outputs;
    }

    public void setOutputs(List<BoxIODefinition> outputs) {
        this.outputs = outputs;
    }

    public List<BoxIODefinition> getConfigurations() {
        return configurations;
    }

    public void setConfigurations(List<BoxIODefinition> configurations) {
        this.configurations = configurations;
    }
}
