package com.aegeanflow.core.session;

import com.google.inject.Inject;

import java.util.UUID;

public class SessionContext {

    private final UUID sessionId;

    @Inject
    public SessionContext(UUID sessionId) {
        this.sessionId = sessionId;
    }

    public UUID getSessionId() {
        return sessionId;
    }
}
