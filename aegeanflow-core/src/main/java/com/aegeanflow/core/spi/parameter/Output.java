package com.aegeanflow.core.spi.parameter;

public class Output<T> extends ParameterImpl<T>{

    protected Output(String name, Class<T> type) {
        super(name, type);
    }
}
