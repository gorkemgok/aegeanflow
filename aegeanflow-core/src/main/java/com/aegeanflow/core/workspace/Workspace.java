package com.aegeanflow.core.workspace;

import com.aegeanflow.core.model.SessionModel;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

public interface Workspace {

    SessionModel save(SessionModel sessionModel) throws IOException;

    SessionModel getFlow(UUID uuid) throws IOException;

    List<SessionModel> getFlowList() throws IOException;

    void changePath(String path);

    String getPath();
}
