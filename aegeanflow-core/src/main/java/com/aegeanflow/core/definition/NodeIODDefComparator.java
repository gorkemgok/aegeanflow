package com.aegeanflow.core.definition;

import org.apache.commons.lang.StringUtils;

import java.util.Comparator;

public class NodeIODDefComparator implements Comparator<NodeIODefinition> {
    public static final NodeIODDefComparator INSTANCE = new NodeIODDefComparator();
    @Override
    public int compare(NodeIODefinition o1, NodeIODefinition o2) {
        if (o1 == null && o2 == null) return 0;
        if (o1 == null) return -1;
        if (o2 == null) return 1;
        if (o1.getOrder() > o2.getOrder()) return 1;
        else if (o1.getOrder() < o2.getOrder()) return -1;
        else {
            return o1.getLabel().compareTo(o2.getLabel());
        }
    }
}
