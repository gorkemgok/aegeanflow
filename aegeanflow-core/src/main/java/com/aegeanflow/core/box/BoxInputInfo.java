package com.aegeanflow.core.box;

import com.aegeanflow.core.box.definition.BoxIODefinition;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

public class BoxInputInfo {

    private final Map<String, Method> methods;

    private final List<BoxIODefinition> ioDefinitionList;

    public BoxInputInfo(Map<String, Method> methods, List<BoxIODefinition> ioDefinitionList) {
        this.methods = methods;
        this.ioDefinitionList = ioDefinitionList;
    }

    public Map<String, Method> getMethods() {
        return methods;
    }

    public List<BoxIODefinition> getIoDefinitionList() {
        return ioDefinitionList;
    }
}
