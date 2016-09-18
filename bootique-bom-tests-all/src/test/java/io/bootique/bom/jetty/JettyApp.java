package io.bootique.bom.jetty;

import io.bootique.jetty.JettyModule;
import io.bootique.jetty.test.junit.JettyTestFactory;

public class JettyApp extends JettyTestFactory {

    @Override
    public Builder app(String... args) {

        return super.app(args).module((binder) -> {
            JettyModule.contributeServlets(binder).addBinding().to(BomServlet.class);
            JettyModule.contributeFilters(binder).addBinding().to(BomFilter.class);
        });
    }
}
