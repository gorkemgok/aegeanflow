package com.aegeanflow.rest;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.UUID;

/**
 * Created by gorkem on 22.01.2018.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class FlowConnectionProxy {

    private UUID uuid;
    private String type;
    private UUID fromUUID;
    private UUID toUUID;
    private String fromName;
    private String toName;

    public UUID getUUID() {
        return uuid;
    }

    public void setUUID(UUID uuid) {
        this.uuid = uuid;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public UUID getFromUUID() {
        return fromUUID;
    }

    public void setFromUUID(UUID fromUUID) {
        this.fromUUID = fromUUID;
    }

    public UUID getToUUID() {
        return toUUID;
    }

    public void setToUUID(UUID toUUID) {
        this.toUUID = toUUID;
    }

    public String getFromName() {
        return fromName;
    }

    public void setFromName(String fromName) {
        this.fromName = fromName;
    }

    public String getToName() {
        return toName;
    }

    public void setToName(String toName) {
        this.toName = toName;
    }
}
