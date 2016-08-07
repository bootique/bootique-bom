package com.nhl.bootique.bom.linkrest;

import com.nhl.bootique.Bootique;
import com.nhl.bootique.bom.linkrest.r1.LrResource1;
import com.nhl.bootique.cayenne.CayenneModule;
import com.nhl.bootique.jdbc.JdbcModule;
import com.nhl.bootique.jersey.JerseyModule;
import com.nhl.bootique.jetty.test.junit.JettyTestFactory;
import com.nhl.bootique.linkrest.LinkRestModule;

import java.util.function.Consumer;

public class LinkRestApp extends JettyTestFactory {

    @Override
    public Builder newRuntime() {

        Consumer<Bootique> config = (bootique) -> {
            bootique.modules(JerseyModule.class, LinkRestModule.class, CayenneModule.class, JdbcModule.class).module((binder) -> {
                JerseyModule.contributeResources(binder).addBinding().to(LrResource1.class);
            });
        };

        return super.newRuntime().configurator(config);
    }

}
