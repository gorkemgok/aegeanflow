package com.aegeanflow.core.workspace;

import com.gorkemgok.annoconf.annotation.ConfigBean;
import com.gorkemgok.annoconf.annotation.ConfigParam;

@ConfigBean
public class WorkspaceConfig {

    public final String store;

    public WorkspaceConfig(
            @ConfigParam(key = "workspace.store", env = "WORKSPACE_STORE") String store) {
        this.store = store;
    }
}
