package com.aegeanflow.core;

import com.aegeanflow.core.box.MathAddBox;
import com.aegeanflow.core.box.StdOutBox;
import com.aegeanflow.core.resource.TableStdOutNode;
import com.aegeanflow.core.spi.Plugin;
import com.aegeanflow.core.spi.box.AnnotatedBox;
import com.aegeanflow.core.spi.node.Node;
import com.aegeanflow.core.table.RandomAccessTable;

import java.util.Arrays;
import java.util.Collection;
import java.util.Map;

public class TestPlugin implements Plugin{
    @Override
    public String name() {
        return "Test Plugin";
    }

    @Override
    public Collection<Class<? extends AnnotatedBox<?>>> provideAnnotatedBoxes() {
        return Arrays.asList(
            MathAddBox.class,
            StdOutBox.class
        );
    }
}
