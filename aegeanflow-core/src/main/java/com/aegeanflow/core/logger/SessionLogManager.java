package com.aegeanflow.core.logger;

import com.aegeanflow.core.session.Session;
import com.aegeanflow.core.spi.node.Node;
import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;

import static java.lang.String.format;

public class SessionLogManager {

    private final Session session;

    @Inject
    public SessionLogManager(@Assisted Session session) {
        this.session = session;
    }

    public void log(Throwable ex) {
        System.out.println(ex.getMessage());
    }

    public void log(Node node) {
        System.out.println(format("%s:%s - %s", node.getName(), node.getState(), node.getId()));
    }
}
