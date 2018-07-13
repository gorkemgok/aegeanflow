package com.aegeanflow.core.proxy;

import com.aegeanflow.core.CoreModule;
import com.aegeanflow.core.Session;
import com.aegeanflow.core.SessionBuilder;
import com.aegeanflow.core.node.MathAddBox;
import com.aegeanflow.core.node.MathAddNode;
import com.aegeanflow.core.node.StdOutBox;
import com.aegeanflow.core.proxy.builder.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.inject.Guice;
import com.google.inject.Injector;
import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.io.IOException;
import java.util.UUID;

public class ProxyTest {

    private UUID addUUID = UUID.randomUUID();
    private UUID add2UUID = UUID.randomUUID();
    private UUID printUUID = UUID.randomUUID();
    private SessionProxy sessionProxy;

    @Test
    public void serializeAndDeserializeCorrectly() throws IOException {
        ObjectMapper json = new ObjectMapper();
        String sessionJson = json.writeValueAsString(sessionProxy);

        SessionProxy sessionProxy = json.readValue(sessionJson, SessionProxy.class);

        Assert.assertEquals(this.sessionProxy.getTitle(), sessionProxy.getTitle());
        System.out.println(sessionJson);
    }

    @Test
    public void runSession() {
        Injector injector = Guice.createInjector(new CoreModule());

        SessionBuilder sessionBuilder = injector.getInstance(SessionBuilder.class);
        Session session = sessionBuilder.buildFrom(sessionProxy);

        session.setInput(addUUID, MathAddNode.IN_1, 4d);
        session.setInput(addUUID, MathAddNode.IN_2, 3d);
        session.run();
    }

    @BeforeTest
    public void setup() {
        sessionProxy = SessionProxyBuilder.aSessionProxy()
            .withTitle("Test Session Proxy")
            .addNode(
                NodeProxyBuilder.aNodeProxy()
                    .withLabel("Addition #1")
                    .withUUID(addUUID)
                    .withType(MathAddNode.class)
                    .withUi(
                        NodeUIProxyBuilder.aNodeUIProxy()
                            .withX(1d).withY(2d).withH(3d).withW(4d).withColor("red")
                            .build()
                    )
                    .build()
            )
            .addNode(
                NodeProxyBuilder.aNodeProxy()
                    .withLabel("Addition #2")
                    .withUUID(add2UUID)
                    .withBoxType(MathAddBox.class)
                    .withUi(
                        NodeUIProxyBuilder.aNodeUIProxy()
                            .withX(10d).withY(20d).withH(30d).withW(40d).withColor("yellow")
                            .build()
                    )
                    .build()
            )
            .addNode(
                NodeProxyBuilder.aNodeProxy()
                    .withLabel("Print to Screen")
                    .withUUID(printUUID)
                    .withBoxType(StdOutBox.class)
                    .withUi(
                        NodeUIProxyBuilder.aNodeUIProxy()
                            .withX(20d).withY(30d).withH(30d).withW(40d).withColor("blue")
                            .build()
                    )
                    .build()
            )
            .addRoute(
                RouteProxyBuilder.aRouteProxy()
                    .withRandomUUID()
                    .withLabel("To input1 of 2. Additions")
                    .withType(RouteProxy.Type.FLOW)
                    .withSource(
                        RoutePointProxyBuilder.aRoutePointProxy()
                            .withUUID(addUUID)
                            .withParameter(MathAddNode.OUT_2.name())
                            .build()
                    )
                    .withTarget(
                        RoutePointProxyBuilder.aRoutePointProxy()
                            .withUUID(add2UUID)
                            .withParameter("input1")
                            .build()
                    )
                    .build()
            )
            .addRoute(
                RouteProxyBuilder.aRouteProxy()
                    .withRandomUUID()
                    .withLabel("To input2 of 2. Additions")
                    .withType(RouteProxy.Type.FLOW)
                    .withSource(
                        RoutePointProxyBuilder.aRoutePointProxy()
                            .withUUID(addUUID)
                            .withParameter(MathAddNode.OUT_2.name())
                            .build()
                    )
                    .withTarget(
                        RoutePointProxyBuilder.aRoutePointProxy()
                            .withUUID(add2UUID)
                            .withParameter("input2")
                            .build()
                    )
                    .build()
            )
            .addRoute(
                RouteProxyBuilder.aRouteProxy()
                    .withRandomUUID()
                    .withLabel("To screen")
                    .withType(RouteProxy.Type.FLOW)
                    .withSource(
                        RoutePointProxyBuilder.aRoutePointProxy()
                            .withUUID(add2UUID)
                            .withParameter("main")
                            .build()
                    )
                    .withTarget(
                        RoutePointProxyBuilder.aRoutePointProxy()
                            .withUUID(printUUID)
                            .withParameter("text")
                            .build()
                    )
                    .build()
            )
            .build();
    }

}