package com.aegeanflow.core.flow;

import java.util.UUID;

/**
 * Created by gorkem on 12.01.2018.
 */
public class FlowConnection {

    private UUID fromUUID;

    private UUID toUUID;

    private String toInput;

    public UUID getFromUUID() {
        return fromUUID;
    }

    public void setFromUUID(UUID fromUUID) {
        this.fromUUID = fromUUID;
    }

    public UUID getToUUID() {
        return toUUID;
    }

    public void setToUUID(UUID toUUID) {
        this.toUUID = toUUID;
    }

    public String getToInput() {
        return toInput;
    }

    public void setToInput(String toInput) {
        this.toInput = toInput;
    }
}
