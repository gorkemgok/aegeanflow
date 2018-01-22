package com.aegeanflow.core.workspace;

import com.google.inject.AbstractModule;
import com.google.inject.Singleton;
import com.gorkemgok.annoconf.annotation.LoadService;

@LoadService(
        autoLoad = true,
        ifConfig = "workspace.type",
        equalsTo = "file"
)
public class FileWorkspaceModule extends AbstractModule{
    @Override
    protected void configure() {
        bind(Workspace.class).to(FileWorkspace.class).in(Singleton.class);
    }
}
