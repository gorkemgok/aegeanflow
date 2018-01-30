package com.aegeanflow.core.spi;

import java.util.UUID;
import java.util.concurrent.Callable;

public interface RunnableNode<T> extends Node, Callable<T>{

    void setUUID(UUID uuid);

    void setName(String name);
}
