package com.aegeanflow.core;

import java.util.UUID;
import java.util.concurrent.Callable;

public interface Node<T> extends Callable<T>{

    void setUUID(UUID uuid);

    UUID getUUID();
}
