package com.aegeanflow.core.session;

import com.aegeanflow.core.exception.IllegalNodeConfigurationException;
import com.aegeanflow.core.model.SessionModel;
import com.google.inject.Inject;
import com.google.inject.Provider;

public class SessionBuilder {

    private final Provider<Session> sessionProvider;

    @Inject
    public SessionBuilder(Provider<Session> sessionProvider) {
        this.sessionProvider = sessionProvider;
    }

    public Session buildFrom(SessionModel proxy) {
        Session session = sessionProvider.get();
        proxy.getNodes().forEach(nodeProxy -> {
            if (nodeProxy.getBoxType() != null) {
                try {
                    session.newBox(
                        nodeProxy.getUUID(),
                        nodeProxy.getBoxType()
                    );
                } catch (IllegalNodeConfigurationException e) {
                    //TODO: log
                    e.printStackTrace();
                }
            } else if (nodeProxy.getType() != null){
                session.newNode(nodeProxy.getUUID(), nodeProxy.getType());
            }
        });

        proxy.getRoutes().forEach(routeProxy -> {
            session.connect(routeProxy.getSource().getUuid(), routeProxy.getSource().getParameter(),
                routeProxy.getTarget().getUuid(), routeProxy.getTarget().getParameter());
        });

        return session;
    }
}
