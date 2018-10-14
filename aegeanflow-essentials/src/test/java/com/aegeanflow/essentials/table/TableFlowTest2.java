package com.aegeanflow.essentials.table;

import com.aegeanflow.core.ioc.AegeanFlowCoreModule;
import com.aegeanflow.core.route.RouteOptions;
import com.aegeanflow.core.route.Router;
import com.aegeanflow.core.session.Session;
import com.aegeanflow.core.spi.node.Node;
import com.aegeanflow.essentials.database.DatabaseConnectionNode;
import com.aegeanflow.essentials.database.DatabaseReaderNode;
import com.aegeanflow.essentials.etl.JoinTableNode;
import com.aegeanflow.essentials.etl.SubstractTablesNode;
import com.aegeanflow.essentials.resource.*;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Provider;
import org.testng.annotations.Guice;
import org.testng.annotations.Test;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Arrays;
import java.util.UUID;

@Guice(modules = AegeanFlowCoreModule.class)
public class TableFlowTest2 {

    @Inject
    Provider<Session> sessionFactory;

    @Inject
    Injector injector;

    @Test
    public void test() throws UnsupportedEncodingException {

    }
}
