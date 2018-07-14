package com.aegeanflow.core.model.builder;

import com.aegeanflow.core.node.AnnotatedNode;
import com.aegeanflow.core.spi.node.Node;
import com.aegeanflow.core.model.NodeModel;
import com.aegeanflow.core.model.NodeUIModel;
import com.aegeanflow.core.spi.box.AnnotatedBox;

import java.util.Map;
import java.util.UUID;

public final class NodeModelBuilder {
    private String label;
    private UUID uuid;
    private Class<? extends Node> type;
    private Class<? extends AnnotatedBox> boxType;
    private Map<String, Object> configuration;
    private NodeUIModel ui;

    private NodeModelBuilder() {
    }

    public static NodeModelBuilder aNodeProxy() {
        return new NodeModelBuilder();
    }

    public NodeModelBuilder withLabel(String label) {
        this.label = label;
        return this;
    }

    public NodeModelBuilder withUUID(UUID uuid) {
        this.uuid = uuid;
        return this;
    }

    public NodeModelBuilder withRandomUUID() {
        this.uuid = UUID.randomUUID();
        return this;
    }

    public NodeModelBuilder withType(Class<? extends Node> type) {
        this.type = type;
        return this;
    }

    public NodeModelBuilder withBoxType(Class<? extends AnnotatedBox> boxType) {
        this.boxType = boxType;
        return withType(AnnotatedNode.class);
    }

    public NodeModelBuilder withConfiguration(Map<String, Object> configuration) {
        this.configuration = configuration;
        return this;
    }

    public NodeModelBuilder withUi(NodeUIModel ui) {
        this.ui = ui;
        return this;
    }

    public NodeModel build() {
        return new NodeModel(label, uuid, type, boxType, configuration, ui);
    }
}
