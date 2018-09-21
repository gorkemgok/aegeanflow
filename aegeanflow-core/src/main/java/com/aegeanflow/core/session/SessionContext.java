package com.aegeanflow.core.session;

import com.aegeanflow.core.logger.SessionLogManager;

public class SessionContext {

    private final Session session;

    private final SessionLogManager logManager;

    public SessionContext(Session session, SessionLogManager logManager) {
        this.session = session;
        this.logManager = logManager;
    }

    public Session getSession() {
        return session;
    }

    public SessionLogManager getLogManager() {
        return logManager;
    }
}
