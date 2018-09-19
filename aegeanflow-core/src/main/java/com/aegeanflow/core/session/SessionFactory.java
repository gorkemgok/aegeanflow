package com.aegeanflow.core.session;

import com.google.inject.Inject;
import com.google.inject.Injector;

public class SessionFactory {

    private final Injector injector;

    @Inject
    public SessionFactory(Injector injector) {
        this.injector = injector;
    }

    public Session create() {
        Injector childInjector = injector.createChildInjector(new SessionModule());
        return childInjector.getInstance(Session.class);
    }
}
