package com.aegeanflow.core.spi.parameter;

import com.google.inject.TypeLiteral;

public class Input<T> extends ParameterImpl<T>{

    protected Input(String name, Class<T> type) {
        super(name, type);
    }

    public Input(String name, TypeLiteral<T> typeLiteral) {
        super(name, typeLiteral);
    }
}
