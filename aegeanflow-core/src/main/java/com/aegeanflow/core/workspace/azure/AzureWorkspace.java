package com.aegeanflow.core.workspace.azure;

import com.aegeanflow.core.model.SessionModel;
import com.aegeanflow.core.workspace.Workspace;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

public class AzureWorkspace implements Workspace{
    @Override
    public SessionModel save(SessionModel sessionModel) throws IOException {
        return null;
    }

    @Override
    public SessionModel getFlow(UUID uuid) throws IOException {
        return null;
    }

    @Override
    public List<SessionModel> getFlowList() throws IOException {
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
