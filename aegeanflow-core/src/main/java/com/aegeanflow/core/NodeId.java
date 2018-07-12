package com.aegeanflow.core;

import java.util.Objects;

public class NodeId {

    private final String packageName;

    private final String name;

    private NodeId(Class<?> clazz) {
        this(clazz.getPackage().getName(), clazz.getName());
    }

    private NodeId(String packageName, String name) {
        this.packageName = packageName;
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        NodeId nodeId = (NodeId) o;
        return Objects.equals(packageName, nodeId.packageName) &&
                Objects.equals(name, nodeId.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(packageName, name);
    }

    @Override
    public String toString() {
        return packageName + "." + name;
    }

    public static NodeId of(Class<?> clazz) {
        return new NodeId(clazz);
    }
}
