package com.aegeanflow.core.proxy.builder;

import com.aegeanflow.core.proxy.NodeProxy;
import com.aegeanflow.core.proxy.RouteProxy;
import com.aegeanflow.core.proxy.SessionProxy;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by gorkem on 12.01.2018.
 */
public class SessionProxyBuilder {

    private final List<NodeProxy> nodeProxyList;

    private final List<RouteProxy> routeProxyList;

    private String title;

    private UUID uuid;

    private NodeProxy lastNode;

    public SessionProxyBuilder() {
        nodeProxyList = new ArrayList<>();
        routeProxyList = new ArrayList<>();
    }

    public SessionProxyBuilder addNode(NodeProxy nodeProxy) {
        nodeProxyList.add(nodeProxy);
        return this;
    }

    public SessionProxyBuilder addRoute(RouteProxy routeProxy) {
        routeProxyList.add(routeProxy);
        return this;
    }

    public SessionProxyBuilder title(String title) {
        this.title = title;
        return this;
    }

    public SessionProxyBuilder uuid(UUID uuid){
        this.uuid = uuid;
        return this;
    }

    public SessionProxyBuilder randomUUID(){
        this.uuid = UUID.randomUUID();
        return this;
    }

    public SessionProxy build() {
        SessionProxy sessionProxy = new SessionProxy(uuid, title, nodeProxyList, routeProxyList);
        return sessionProxy;
    }
}
