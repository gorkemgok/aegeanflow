package com.aegeanflow.essentials;

import com.aegeanflow.core.spi.node.Node;
import com.aegeanflow.core.spi.box.AnnotatedBox;
import com.aegeanflow.core.spi.Plugin;
import com.aegeanflow.essentials.box.*;
import com.aegeanflow.essentials.node.SomeNode;
import com.sun.javafx.collections.UnmodifiableObservableMap;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class EssentialsPlugin implements Plugin{
    @Override
    public String name() {
        return "essentials";
    }

    @Override
    public Collection<Class<? extends AnnotatedBox<?>>> provideAnnotatedBoxes() {
        return Arrays.asList(
                ColumnGeneratorBox.class,
                ConvertToStringBox.class,
                DatabaseConnectionBox.class,
                DatabaseReaderBox.class,
                DatabaseWriterBox.class,
                DataSchemarBox.class,
                StdOutBox.class,
                TabularDataAparter.class,
                TabularDataInfoBox.class,
                TabularDataMergeBox.class,
                TabularDataSplitterBox.class,
                UUIDGeneratorBox.class
        );
    }

    @Override
    public Map<String, Class<? extends Node>> provideNodeClasses() {
        Map<String, Class<? extends Node>> map = new HashMap<>();
        map.put(SomeNode.NAME, SomeNode.class);
        return map;
    }
}
