package com.aegeanflow.core.spi.parameter;

public class Input<T> extends ParameterImpl<T>{

    protected Input(String name, Class<T> type) {
        super(name, type);
    }
}
