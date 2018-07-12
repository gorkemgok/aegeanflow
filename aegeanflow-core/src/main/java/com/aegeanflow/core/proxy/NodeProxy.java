package com.aegeanflow.core.proxy;

import com.aegeanflow.core.json.ClassDeserializer;
import com.aegeanflow.core.json.ClassSerializer;
import com.aegeanflow.core.spi.AnnotatedBox;
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
public class NodeProxy {

    private final String name;

    private final UUID uuid;

    private final Class<? extends AnnotatedBox> type;

    private final Map<String, Object> configuration;

    private final NodeUIProxy ui;

    @JsonCreator
    public NodeProxy(
            @JsonProperty String name,
            @JsonProperty UUID uuid,
            @JsonProperty Class<? extends AnnotatedBox> type,
            @JsonProperty Map<String, Object> configuration,
            @JsonProperty NodeUIProxy ui) {
        this.name = name;
        this.uuid = uuid;
        this.type = type;
        this.configuration = configuration;
        this.ui = ui;
    }

    public String getName() {
        return name;
    }

    public Map<String, Object> getConfiguration() {
        return configuration;
    }

    public UUID getUUID() {
        return uuid;
    }

    @JsonSerialize(using = ClassSerializer.class)
    @JsonDeserialize(using = ClassDeserializer.class)
    public Class<? extends AnnotatedBox> getType() {
        return type;
    }

}
