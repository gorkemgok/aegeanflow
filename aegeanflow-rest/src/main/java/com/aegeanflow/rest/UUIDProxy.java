package com.aegeanflow.rest;

import java.util.UUID;

/**
 * Created by gorkem on 23.01.2018.
 */
public class UUIDProxy {

    private UUID uuid;

    public UUIDProxy() {
    }

    public UUIDProxy(UUID uuid) {
        this.uuid = uuid;
    }

    public UUID getUuid() {
        return uuid;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }
}
