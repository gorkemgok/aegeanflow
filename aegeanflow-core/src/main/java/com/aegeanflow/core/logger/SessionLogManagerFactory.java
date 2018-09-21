package com.aegeanflow.core.logger;

import com.aegeanflow.core.session.Session;

public interface SessionLogManagerFactory {

    SessionLogManager create(Session session);
}
