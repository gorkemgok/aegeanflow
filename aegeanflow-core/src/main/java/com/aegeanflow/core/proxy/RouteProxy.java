package com.aegeanflow.core.proxy;

import java.util.UUID;

/**
 * Created by gorkem on 12.01.2018.
 */
public class RouteProxy {

    public enum Type {
        FLOW, WAIT
    }

    private UUID uuid;

    private Type type;

    private UUID fromUUID;

    private UUID toUUID;

    private String outputName;

    private String inputName;

    public String getOutputName() {
        return outputName;
    }

    public void setOutputName(String outputName) {
        this.outputName = outputName;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public UUID getUuid() {
        return uuid;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
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

    public String getInputName() {
        return inputName;
    }

    public void setInputName(String inputName) {
        this.inputName = inputName;
    }
}