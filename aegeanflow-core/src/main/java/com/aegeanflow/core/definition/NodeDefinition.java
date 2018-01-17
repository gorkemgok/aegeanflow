package com.aegeanflow.core.definition;

import com.aegeanflow.core.spi.Node;

import java.util.List;

public class NodeDefinition {

    private Class<? extends Node> type;

    private String label;

    private List<NodeInputDefinition> inputs;

    private List<NodeConfigurationDefinition> configurations;

    private Class<?> returnType;

    public Class<?> getReturnType() {
        return returnType;
    }

    public void setReturnType(Class<?> returnType) {
        this.returnType = returnType;
    }

    public Class<? extends Node> getType() {
        return type;
    }

    public void setType(Class<? extends Node> type) {
        this.type = type;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public List<NodeInputDefinition> getInputs() {
        return inputs;
    }

    public void setInputs(List<NodeInputDefinition> inputs) {
        this.inputs = inputs;
    }

    public List<NodeConfigurationDefinition> getConfigurations() {
        return configurations;
    }

    public void setConfigurations(List<NodeConfigurationDefinition> configurations) {
        this.configurations = configurations;
    }
}
