package com.aegeanflow.core.box.definition;

import java.util.Comparator;

public class BoxIODDefComparator implements Comparator<BoxIODefinition> {
    public static final BoxIODDefComparator INSTANCE = new BoxIODDefComparator();
    @Override
    public int compare(BoxIODefinition o1, BoxIODefinition o2) {
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
