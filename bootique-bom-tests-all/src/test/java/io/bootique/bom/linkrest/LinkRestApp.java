package io.bootique.bom.linkrest;

import io.bootique.bom.linkrest.r1.LrResource1;
import io.bootique.cayenne.CayenneModule;
import io.bootique.jdbc.JdbcModule;
import io.bootique.jersey.JerseyModule;
import io.bootique.jetty.test.junit.JettyTestFactory;
import io.bootique.linkrest.LinkRestModule;

public class LinkRestApp extends JettyTestFactory {

    @Override
    public Builder app(String... args) {

        return super.app(args)
                .modules(JerseyModule.class, LinkRestModule.class, CayenneModule.class, JdbcModule.class)
                .module((binder) -> {
                    JerseyModule.contributeResources(binder).addBinding().to(LrResource1.class);
                });
    }

}
