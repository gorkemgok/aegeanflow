package com.aegeanflow.core.node;

import com.aegeanflow.core.spi.AbstractNode;
import com.aegeanflow.core.spi.annotation.NodeEntry;

import java.util.UUID;

/**
 * Created by gorkem on 29.01.2018.
 */
@NodeEntry
public class UUIDGeneratorNode extends AbstractNode<UUID> {
    @Override
    public UUID call() throws Exception {
        return UUID.randomUUID();
    }
}
