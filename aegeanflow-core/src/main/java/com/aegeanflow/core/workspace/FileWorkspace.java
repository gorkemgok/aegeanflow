package com.aegeanflow.core.workspace;

import com.aegeanflow.core.flow.Flow;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static java.lang.String.format;


public class FileWorkspace implements Workspace{

    private static final Logger LOGGER = LoggerFactory.getLogger(FileWorkspace.class);

    private ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public Flow save(Flow flow) throws IOException {
        if (flow.getUuid() == null) {
            flow.setUuid(UUID.randomUUID());
        }
        try {
            objectMapper.writeValue(new File(format("%s.aflow", flow.getUuid().toString())), flow);
            return flow;
        } catch (IOException e) {
            throw e;
        }
    }

    @Override
    public Flow getFlow(UUID uuid) throws IOException{
        Flow flow = objectMapper.readValue(new File(format("%s.aflow", uuid.toString())), Flow.class);
        return flow;
    }

    @Override
    public List<Flow> getFlowList() throws IOException {
        File dir = new File("./");
        File[] files = dir.listFiles(pathname -> pathname.getName().endsWith(".aflow"));
        List<Flow> flowList = new ArrayList<>();
        for (File file : files) {
            Flow flow = objectMapper.readValue(file, Flow.class);
            flowList.add(flow);
        }
        return flowList;
    }
}
