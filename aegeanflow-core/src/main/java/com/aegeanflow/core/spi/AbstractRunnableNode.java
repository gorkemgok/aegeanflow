package com.aegeanflow.core.spi;

import java.util.UUID;

public abstract class AbstractRunnableNode<T> implements RunnableNode<T> {

    private UUID uuid;

    private String name;

    @Override
    public Class<? extends RunnableNode> getNodeClass() {
        return this.getClass();
    }

    @Override
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public void setUUID(UUID uuid) {
        this.uuid = uuid;
    }

    @Override
    public UUID getUUID() {
        return uuid;
    }
}
