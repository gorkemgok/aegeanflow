package com.aegeanflow.core.workspace.azure;

import com.aegeanflow.core.flow.Flow;
import com.aegeanflow.core.workspace.Workspace;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

public class AzureWorkspace implements Workspace{
    @Override
    public Flow save(Flow flow) throws IOException {
        return null;
    }

    @Override
    public Flow getFlow(UUID uuid) throws IOException {
        return null;
    }

    @Override
    public List<Flow> getFlowList() throws IOException {
        return null;
    }

    @Override
    public void changePath(String path) {

    }

    @Override
    public String getPath() {
        return null;
    }
}
