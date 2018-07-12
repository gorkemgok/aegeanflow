package com.aegeanflow.core.workspace;

import com.aegeanflow.core.proxy.SessionProxy;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

public interface Workspace {

    SessionProxy save(SessionProxy sessionProxy) throws IOException;

    SessionProxy getFlow(UUID uuid) throws IOException;

    List<SessionProxy> getFlowList() throws IOException;

    void changePath(String path);

    String getPath();
}
