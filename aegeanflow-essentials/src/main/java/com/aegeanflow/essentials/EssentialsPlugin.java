package com.aegeanflow.essentials;

import com.aegeanflow.core.Node;
import com.aegeanflow.core.spi.AnnotatedBox;
import com.aegeanflow.core.spi.Plugin;
import com.aegeanflow.essentials.box.*;
import com.aegeanflow.essentials.node.SomeNode;

import java.util.Arrays;
import java.util.Collection;

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
    public Collection<Class<? extends Node>> provideNodeClasses() {
        return Arrays.asList(
                SomeNode.class
        );
    }
}
