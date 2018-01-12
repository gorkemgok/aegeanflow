package com.aegeanflow.core.exception;

import java.util.UUID;

/**
 * Created by gorkem on 12.01.2018.
 */
public class NoSuchNodeException extends Exception{

    private final UUID uuid;

    public NoSuchNodeException(UUID uuid) {
        this.uuid = uuid;
    }

    public UUID getUuid() {
        return uuid;
    }
}
