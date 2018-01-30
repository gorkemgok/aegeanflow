package com.aegeanflow.core.definition;

import com.aegeanflow.core.spi.RunnableNode;

import java.util.List;

public class NodeDefinition {

    private Class<? extends RunnableNode> type;

    private String label;

    private List<NodeIODefinition> inputs;

    private List<NodeIODefinition> outputs;

    private List<NodeConfigurationDefinition> configurations;

    public Class<? extends RunnableNode> getType() {
        return type;
    }

    public void setType(Class<? extends RunnableNode> type) {
        this.type = type;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public List<NodeIODefinition> getInputs() {
        return inputs;
    }

    public void setInputs(List<NodeIODefinition> inputs) {
        this.inputs = inputs;
    }

    public List<NodeIODefinition> getOutputs() {
        return outputs;
    }

    public void setOutputs(List<NodeIODefinition> outputs) {
        this.outputs = outputs;
    }

    public List<NodeConfigurationDefinition> getConfigurations() {
        return configurations;
    }

    public void setConfigurations(List<NodeConfigurationDefinition> configurations) {
        this.configurations = configurations;
    }
}
