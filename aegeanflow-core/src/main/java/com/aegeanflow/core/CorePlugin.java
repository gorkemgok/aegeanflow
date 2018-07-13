package com.aegeanflow.core;

import com.aegeanflow.core.spi.Plugin;

public class CorePlugin implements Plugin{

    public static final String NAME = "Core Plugin";

    @Override
    public String name() {
        return NAME;
    }
}
