package com.aegeanflow.core.workspace.file;

import com.aegeanflow.core.model.SessionModel;
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
    public SessionModel save(SessionModel sessionModel) throws IOException {
        try {
            //TODO: fix null
            objectMapper.writeValue(new File(format("%s%s%s.aflow", path, File.separator, UUID.randomUUID())), sessionModel);
            return sessionModel;
        } catch (IOException e) {
            throw e;
        }
    }

    @Override
    public SessionModel getFlow(UUID uuid) throws IOException{
        SessionModel sessionModel = objectMapper.readValue(new File(format("%s.aflow", uuid.toString())), SessionModel.class);
        return sessionModel;
    }

    @Override
    public List<SessionModel> getFlowList() throws IOException {
        File dir = new File(path);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        File[] files = dir.listFiles(pathname -> pathname.getName().endsWith(".aflow"));
        List<SessionModel> sessionModelList = new ArrayList<>();
        for (File file : files) {
            SessionModel sessionModel = objectMapper.readValue(file, SessionModel.class);
            sessionModelList.add(sessionModel);
        }
        return sessionModelList;
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
