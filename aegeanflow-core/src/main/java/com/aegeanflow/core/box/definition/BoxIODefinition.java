package com.aegeanflow.core.box.definition;

import java.lang.reflect.Method;

public class BoxIODefinition {

    public BoxIODefinition(String name, Class<?> type, String label, int order, Method method, InputType inputType) {
        this.name = name;
        this.type = type;
        this.label = label;
        this.order = order;
        this.method = method;
        this.inputType = inputType;
    }

    public enum InputType { INPUT, CONGIF}

    private final String name;

    private final Class<?> type;

    private final String label;

    private final int order;

    private final Method method;

    private final InputType inputType;

    public InputType getInputType() {
        return inputType;
    }

    public Method getMethod() {
        return method;
    }

    public int getOrder() {
        return order;
    }

    public String getName() {
        return name;
    }

    public Class<?> getType() {
        return type;
    }

    public String getLabel() {
        return label;
    }

}
