package com.aegeanflow.core.exception;

import java.util.UUID;

/**
 * Created by gorkem on 23.01.2018.
 */
public class NodePreconditionException extends NodeRuntimeException {

    public NodePreconditionException(String message, UUID nodeUUID) {
        super(message, nodeUUID);
    }
}
