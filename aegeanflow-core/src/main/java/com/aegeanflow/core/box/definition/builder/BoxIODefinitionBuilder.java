package com.aegeanflow.core.box.definition.builder;

import com.aegeanflow.core.box.definition.BoxIODefinition;

import java.lang.reflect.Method;

public final class BoxIODefinitionBuilder {
    private String name;
    private Class<?> type;
    private String label;
    private int order;
    private Method method;
    private BoxIODefinition.InputType inputType;

    private BoxIODefinitionBuilder() {
    }

    public static BoxIODefinitionBuilder aBoxIODefinition() {
        return new BoxIODefinitionBuilder();
    }

    public BoxIODefinitionBuilder withName(String name) {
        this.name = name;
        return this;
    }

    public BoxIODefinitionBuilder withType(Class<?> type) {
        this.type = type;
        return this;
    }

    public BoxIODefinitionBuilder withLabel(String label) {
        this.label = label;
        return this;
    }

    public BoxIODefinitionBuilder withOrder(int order) {
        this.order = order;
        return this;
    }

    public BoxIODefinitionBuilder withMethod(Method method) {
        this.method = method;
        return this;
    }

    public BoxIODefinitionBuilder withInputType(BoxIODefinition.InputType inputType) {
        this.inputType = inputType;
        return this;
    }

    public BoxIODefinition build() {
        return new BoxIODefinition(name, type, label, order, method, inputType);
    }
}
