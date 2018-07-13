package com.aegeanflow.essentials.box;

import com.aegeanflow.core.exchange.Exchange;
import com.aegeanflow.core.spi.AbstractAnnotatedBox;
import com.aegeanflow.core.spi.annotation.NodeEntry;

import java.util.UUID;

/**
 * Created by gorkem on 29.01.2018.
 */
@NodeEntry(name = "UUID Generator")
public class UUIDGeneratorBox extends AbstractAnnotatedBox<UUID> {
    @Override
    public Exchange<UUID> call() throws Exception {
        return Exchange.createUnpersistent(UUID.randomUUID());
    }
}
