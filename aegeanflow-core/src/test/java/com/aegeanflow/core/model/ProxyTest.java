package com.aegeanflow.core.model;

import com.aegeanflow.core.TestModule;
import com.aegeanflow.core.ioc.CoreModule;
import com.aegeanflow.core.session.Session;
import com.aegeanflow.core.session.SessionBuilder;
import com.aegeanflow.core.box.MathAddBox;
import com.aegeanflow.core.box.StdOutBox;
import com.aegeanflow.core.model.builder.*;
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
    private SessionModel sessionModel;
    private Injector injector;

    @Test
    public void serializeAndDeserializeCorrectly() throws IOException {
        ObjectMapper json = new ObjectMapper();
        String sessionJson = json.writeValueAsString(this.sessionModel);

        SessionModel sessionModel = json.readValue(sessionJson, SessionModel.class);

        Assert.assertEquals(this.sessionModel.getTitle(), sessionModel.getTitle());
        System.out.println(sessionJson);
    }

    @Test
    public void runSession() {
        SessionBuilder sessionBuilder = injector.getInstance(SessionBuilder.class);
        Session session = sessionBuilder.buildFrom(sessionModel);

        session.setInput(addUUID, MathAddNode.IN_1, 4d);
        session.setInput(addUUID, MathAddNode.IN_2, 3d);
        session.run();
    }

    @BeforeTest
    public void setup() {
        injector = Guice.createInjector(new CoreModule(), new TestModule());
        sessionModel = SessionModelBuilder.aSessionProxy()
            .withRandomUUID()
            .withTitle("Test Session Proxy")
            .addNode(
                NodeModelBuilder.aNodeProxy()
                    .withLabel("Addition #1")
                    .withUUID(addUUID)
                    .withType(MathAddNode.class)
                    .withUi(
                        NodeUIModelBuilder.aNodeUIProxy()
                            .withX(1d).withY(2d).withH(3d).withW(4d).withColor("red")
                            .build()
                    )
                    .build()
            )
            .addNode(
                NodeModelBuilder.aNodeProxy()
                    .withLabel("Addition #2")
                    .withUUID(add2UUID)
                    .withBoxType(MathAddBox.class)
                    .withUi(
                        NodeUIModelBuilder.aNodeUIProxy()
                            .withX(10d).withY(20d).withH(30d).withW(40d).withColor("yellow")
                            .build()
                    )
                    .build()
            )
            .addNode(
                NodeModelBuilder.aNodeProxy()
                    .withLabel("Print to Screen")
                    .withUUID(printUUID)
                    .withBoxType(StdOutBox.class)
                    .withUi(
                        NodeUIModelBuilder.aNodeUIProxy()
                            .withX(20d).withY(30d).withH(30d).withW(40d).withColor("blue")
                            .build()
                    )
                    .build()
            )
            .addRoute(
                RouteModelBuilder.aRouteProxy()
                    .withRandomUUID()
                    .withLabel("To input1 of 2. Additions")
                    .withType(RouteModel.Type.FLOW)
                    .withSource(
                        RoutePointModeBuilder.aRoutePointProxy()
                            .withUUID(addUUID)
                            .withParameter(MathAddNode.OUT_2.name())
                            .build()
                    )
                    .withTarget(
                        RoutePointModeBuilder.aRoutePointProxy()
                            .withUUID(add2UUID)
                            .withParameter("input1")
                            .build()
                    )
                    .build()
            )
            .addRoute(
                RouteModelBuilder.aRouteProxy()
                    .withRandomUUID()
                    .withLabel("To input2 of 2. Additions")
                    .withType(RouteModel.Type.FLOW)
                    .withSource(
                        RoutePointModeBuilder.aRoutePointProxy()
                            .withUUID(addUUID)
                            .withParameter(MathAddNode.OUT_2.name())
                            .build()
                    )
                    .withTarget(
                        RoutePointModeBuilder.aRoutePointProxy()
                            .withUUID(add2UUID)
                            .withParameter("input2")
                            .build()
                    )
                    .build()
            )
            .addRoute(
                RouteModelBuilder.aRouteProxy()
                    .withRandomUUID()
                    .withLabel("To screen")
                    .withType(RouteModel.Type.FLOW)
                    .withSource(
                        RoutePointModeBuilder.aRoutePointProxy()
                            .withUUID(add2UUID)
                            .withParameter("main")
                            .build()
                    )
                    .withTarget(
                        RoutePointModeBuilder.aRoutePointProxy()
                            .withUUID(printUUID)
                            .withParameter("text")
                            .build()
                    )
                    .build()
            )
            .build();
    }

}