package com.aegeanflow.core;

import java.util.Objects;

public class ParameterImpl implements Parameter{

    private final String name;

    private final Class<?> type;

    protected ParameterImpl(String name, Class<?> type) {
        this.name = name;
        this.type = type;
    }

    public String name() {
        return name;
    }

    public Class<?> type() {
        return type;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ParameterImpl parameter = (ParameterImpl) o;
        return Objects.equals(name, parameter.name) &&
                Objects.equals(type, parameter.type);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, type);
    }
}
