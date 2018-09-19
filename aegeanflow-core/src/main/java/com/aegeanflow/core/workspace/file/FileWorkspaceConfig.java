package com.aegeanflow.core.workspace.file;

import com.gorkemgok.annoconf.annotation.ConfigBean;
import com.gorkemgok.annoconf.annotation.ConfigParam;

@ConfigBean
public class FileWorkspaceConfig {

    public final String path;

    public FileWorkspaceConfig(
            @ConfigParam(keys = {"workspace.file.path", "WORKSPACE_PATH"}, defaultValue = "./workspace") String path) {
        this.path = path;
    }
}
