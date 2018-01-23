package com.aegeanflow.rest;

import java.util.UUID;

/**
 * Created by gorkem on 23.01.2018.
 */
public class NodeErrorProxy extends ErrorProxy {

    private UUID uuid;

    public NodeErrorProxy() {
    }

    public NodeErrorProxy(UUID uuid) {
        this.uuid = uuid;
    }

    public NodeErrorProxy(UUID uuid, String message) {
        super(message);
        this.uuid = uuid;
    }

    public UUID getUuid() {
        return uuid;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }
}
