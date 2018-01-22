package com.aegeanflow.core.workspace;

import com.aegeanflow.core.flow.Flow;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

public interface Workspace {

    Flow save(Flow flow) throws IOException;

    Flow getFlow(UUID uuid) throws IOException;

    List<Flow> getFlowList() throws IOException;

}
