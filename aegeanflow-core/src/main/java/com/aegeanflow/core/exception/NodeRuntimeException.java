package com.aegeanflow.core.exception;

import java.util.UUID;

/**
 * Created by gorkem on 12.01.2018.
 */
public class NodeRuntimeException extends RuntimeException {

    private final UUID nodeUUID;

    public NodeRuntimeException(UUID nodeUUID) {
        this.nodeUUID = nodeUUID;
    }

    public NodeRuntimeException(String message, UUID nodeUUID) {
        super(message);
        this.nodeUUID = nodeUUID;
    }

    public NodeRuntimeException(Throwable cause, UUID nodeUUID) {
        super(cause);
        this.nodeUUID = nodeUUID;
    }

    public UUID getNodeUUID() {
        return nodeUUID;
    }
}
