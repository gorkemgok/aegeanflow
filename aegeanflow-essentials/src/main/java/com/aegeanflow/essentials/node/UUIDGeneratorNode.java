package com.aegeanflow.essentials.node;

import com.aegeanflow.core.spi.AbstractRunnableNode;
import com.aegeanflow.core.spi.annotation.NodeEntry;

import java.util.UUID;

/**
 * Created by gorkem on 29.01.2018.
 */
@NodeEntry(label = "UUID Generator")
public class UUIDGeneratorNode extends AbstractRunnableNode<UUID> {
    @Override
    public UUID call() throws Exception {
        return UUID.randomUUID();
    }
}
