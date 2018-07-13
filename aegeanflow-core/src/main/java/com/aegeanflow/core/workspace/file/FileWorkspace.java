package com.aegeanflow.core.workspace.file;

import com.aegeanflow.core.proxy.SessionProxy;
import com.aegeanflow.core.workspace.Workspace;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.inject.Inject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static java.lang.String.format;


public class FileWorkspace implements Workspace {

    private static final Logger LOGGER = LoggerFactory.getLogger(FileWorkspace.class);

    private final ObjectMapper objectMapper;

    private String path;

    @Inject
    public FileWorkspace(ObjectMapper objectMapper, FileWorkspaceConfig config) {
        this.objectMapper = objectMapper;
        this.path = config.path;
    }

    @Override
    public SessionProxy save(SessionProxy sessionProxy) throws IOException {
        try {
            //TODO: fix null
            objectMapper.writeValue(new File(format("%s%s%s.aflow", path, File.separator, null)), sessionProxy);
            return sessionProxy;
        } catch (IOException e) {
            throw e;
        }
    }

    @Override
    public SessionProxy getFlow(UUID uuid) throws IOException{
        SessionProxy sessionProxy = objectMapper.readValue(new File(format("%s.aflow", uuid.toString())), SessionProxy.class);
        return sessionProxy;
    }

    @Override
    public List<SessionProxy> getFlowList() throws IOException {
        File dir = new File(path);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        File[] files = dir.listFiles(pathname -> pathname.getName().endsWith(".aflow"));
        List<SessionProxy> sessionProxyList = new ArrayList<>();
        for (File file : files) {
            SessionProxy sessionProxy = objectMapper.readValue(file, SessionProxy.class);
            sessionProxyList.add(sessionProxy);
        }
        return sessionProxyList;
    }

    @Override
    public void changePath(String path) {
        this.path = path;
    }

    @Override
    public String getPath() {
        return path;
    }
}
