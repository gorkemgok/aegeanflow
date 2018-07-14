package com.aegeanflow.core.model;

import com.aegeanflow.core.spi.node.Node;
import com.aegeanflow.core.json.ClassDeserializer;
import com.aegeanflow.core.json.ClassSerializer;
import com.aegeanflow.core.spi.box.AnnotatedBox;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.util.Map;
import java.util.UUID;

/**
 * Created by gorkem on 12.01.2018.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class NodeModel extends IdModel {

    private final String label;

    private final UUID uuid;

    private final Class<? extends Node> type;

    private final Class<? extends AnnotatedBox> boxType;

    private final Map<String, Object> configuration;

    private final NodeUIModel ui;

    @JsonCreator
    public NodeModel(
            @JsonProperty("label") String label,
            @JsonProperty("uuid") UUID uuid,
            @JsonProperty("type") Class<? extends Node> type,
            @JsonProperty("boxType") Class<? extends AnnotatedBox> boxType,
            @JsonProperty("configuration") Map<String, Object> configuration,
            @JsonProperty("ui") NodeUIModel ui) {
        this.label = label;
        this.uuid = uuid;
        this.type = type;
        this.boxType = boxType;
        this.configuration = configuration;
        this.ui = ui;
    }

    public String getLabel() {
        return label;
    }

    public Map<String, Object> getConfiguration() {
        return configuration;
    }

    public UUID getUUID() {
        return uuid;
    }

    @JsonSerialize(using = ClassSerializer.class)
    @JsonDeserialize(using = ClassDeserializer.class)
    public Class<? extends Node> getType() {
        return type;
    }

    @JsonSerialize(using = ClassSerializer.class)
    @JsonDeserialize(using = ClassDeserializer.class)
    public Class<? extends AnnotatedBox> getBoxType() {
        return boxType;
    }

    public NodeUIModel getUi() {
        return ui;
    }
}
