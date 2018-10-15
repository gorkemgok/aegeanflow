package com.aegeanflow.core.logger;

import com.aegeanflow.core.session.Session;
import com.aegeanflow.core.spi.node.Node;
import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static java.lang.String.format;

public class SessionLogManager {

    private static final Logger LOGGER = LoggerFactory.getLogger(SessionLogManager.class);

    private final Session session;

    @Inject
    public SessionLogManager(@Assisted Session session) {
        this.session = session;
    }

    public void log(Throwable ex) {
        LOGGER.error(ex.getMessage());
    }

    public void log(Node node) {
        LOGGER.info(format("%s:%s - %s", node.getName(), node.getState(), node.getId()));
    }
}
