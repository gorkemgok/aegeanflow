package com.aegeanflow.core.spi;

import com.aegeanflow.core.spi.Node;

import java.util.UUID;

public abstract class AbstractNode<T> implements Node<T> {

    private UUID uuid;

    @Override
    public void setUUID(UUID uuid) {
        this.uuid = uuid;
    }

    @Override
    public UUID getUUID() {
        return uuid;
    }
}
