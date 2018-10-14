package com.aegeanflow.core.spi.parameter;

import com.google.inject.TypeLiteral;

public class Output<T> extends ParameterImpl<T>{

    protected Output(String name, Class<T> type) {
        super(name, type);
    }

    public Output(String name, TypeLiteral<T> typeLiteral) {
        super(name, typeLiteral);
    }
}
