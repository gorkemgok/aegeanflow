package com.aegeanflow.core.spi.parameter;

import com.google.inject.TypeLiteral;

import java.util.Objects;

public class ParameterImpl<T> implements Parameter<T> {

    private final String name;

    private final Class<T> type;

    private final TypeLiteral<T> typeLiteral;

    protected ParameterImpl(String name, Class<T> type) {
        this.name = name;
        this.type = type;
        this.typeLiteral = null;
    }

    protected ParameterImpl(String name, TypeLiteral<T> typeLiteral) {
        this.name = name;
        this.type = null;
        this.typeLiteral = typeLiteral;
    }

    public String name() {
        return name;
    }

    public Class<T> type() {
        return type;
    }

    @Override
    public TypeLiteral<T> typeLiteral() {
        return typeLiteral;
    }

    public boolean isAssignable(Parameter parameter) {
        if (this == parameter) return true;
        if (parameter == null || getClass() != parameter.getClass()) return false;
        return Objects.equals(name, parameter.name()) &&
                ((type != null && type.isAssignableFrom(parameter.type())) ||
                        (typeLiteral != null && typeLiteral.equals(parameter.typeLiteral())));
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
