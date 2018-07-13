package com.aegeanflow.core.spi;

import com.aegeanflow.core.exchange.Exchange;

import java.util.UUID;
import java.util.concurrent.Callable;

public interface AnnotatedBox<T> extends Callable<Exchange<T>>{

    void setUUID(UUID uuid);

    void setName(String name);

    UUID getUUID();

    Class<? extends AnnotatedBox> getNodeClass();
}
