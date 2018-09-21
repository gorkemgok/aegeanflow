package com.aegeanflow.core.session;

import com.aegeanflow.core.logger.SessionLogManager;
import com.google.inject.AbstractModule;


public class SessionModule extends AbstractModule {

    private final Session session;

    private final SessionLogManager logManager;

    public SessionModule(Session session, SessionLogManager logManager) {
        this.session = session;
        this.logManager = logManager;
    }

    @Override
    protected void configure() {
        bind(SessionContext.class)
                .toInstance(new SessionContext(session, logManager));
    }

}
