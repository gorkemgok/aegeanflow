package com.aegeanflow.core.spi;

import java.util.UUID;

/**
 * Created by gorkem on 30.01.2018.
 */
public interface Node {

    UUID getUUID();

    Class<? extends RunnableNode> getNodeClass();

    String getName();
}
