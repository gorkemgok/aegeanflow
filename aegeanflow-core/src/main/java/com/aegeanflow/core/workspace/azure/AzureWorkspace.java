package com.aegeanflow.core.workspace.azure;

import com.aegeanflow.core.proxy.SessionProxy;
import com.aegeanflow.core.workspace.Workspace;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

public class AzureWorkspace implements Workspace{
    @Override
    public SessionProxy save(SessionProxy sessionProxy) throws IOException {
        return null;
    }

    @Override
    public SessionProxy getFlow(UUID uuid) throws IOException {
        return null;
    }

    @Override
    public List<SessionProxy> getFlowList() throws IOException {
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
