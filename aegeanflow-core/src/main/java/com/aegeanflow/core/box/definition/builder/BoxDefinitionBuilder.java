package com.aegeanflow.core.box.definition.builder;

import com.aegeanflow.core.box.definition.BoxDefinition;
import com.aegeanflow.core.box.definition.BoxIODefinition;
import com.aegeanflow.core.spi.box.AnnotatedBox;

import java.util.List;

public final class BoxDefinitionBuilder {
    private Class<? extends AnnotatedBox> type;
    private String name;
    private List<BoxIODefinition> inputs;
    private List<BoxIODefinition> outputs;

    private BoxDefinitionBuilder() {
    }

    public static BoxDefinitionBuilder aBoxDefinition() {
        return new BoxDefinitionBuilder();
    }

    public BoxDefinitionBuilder withType(Class<? extends AnnotatedBox> type) {
        this.type = type;
        return this;
    }

    public BoxDefinitionBuilder withName(String name) {
        this.name = name;
        return this;
    }

    public BoxDefinitionBuilder withInputs(List<BoxIODefinition> inputs) {
        this.inputs = inputs;
        return this;
    }

    public BoxDefinitionBuilder withOutputs(List<BoxIODefinition> outputs) {
        this.outputs = outputs;
        return this;
    }

    public BoxDefinition build() {
        return new BoxDefinition(type, name, inputs, outputs);
    }
}
