package com.aegeanflow.core.workspace.file;

import com.aegeanflow.core.workspace.Workspace;
import com.google.inject.AbstractModule;
import com.google.inject.Singleton;
import com.gorkemgok.annoconf.annotation.LoadService;

@LoadService(
        autoLoad = true,
        ifConfig = "workspace.store",
        equalsTo = "file"
)
public class FileWorkspaceModule extends AbstractModule{
    @Override
    protected void configure() {
        bind(Workspace.class).to(FileWorkspace.class).in(Singleton.class);
    }
}
