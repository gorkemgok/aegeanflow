package com.aegeanflow.core.route;

import com.aegeanflow.core.session.Session;

public interface RouterFactory {

    Router create(Session session);

}
