package com.aegeanflow.core;

import java.util.Objects;

public class ParameterImpl<T> implements Parameter<T>{

    private final String name;

    private final Class<T> type;

    protected ParameterImpl(String name, Class<T> type) {
        this.name = name;
        this.type = type;
    }

    public String name() {
        return name;
    }

    public Class<T> type() {
        return type;
    }

    public boolean isAssignable(Parameter parameter) {
        if (this == parameter) return true;
        if (parameter == null || getClass() != parameter.getClass()) return false;
        return Objects.equals(name, parameter.name()) &&
                type.isAssignableFrom(parameter.type());
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
