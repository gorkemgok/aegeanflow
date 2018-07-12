package com.aegeanflow.essentials.node;

import com.aegeanflow.core.Exchange;
import com.aegeanflow.core.spi.AbstractAnnotatedBox;
import com.aegeanflow.core.spi.annotation.NodeEntry;

import java.util.UUID;

/**
 * Created by gorkem on 29.01.2018.
 */
@NodeEntry(label = "UUID Generator")
public class UUIDGeneratorBox extends AbstractAnnotatedBox<UUID> {
    @Override
    public Exchange<UUID> call() throws Exception {
        return Exchange.create(UUID.randomUUID());
    }
}
