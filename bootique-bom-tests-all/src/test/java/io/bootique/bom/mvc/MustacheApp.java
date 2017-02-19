package io.bootique.bom.mvc;

import io.bootique.bom.mvc.r1.MustacheResource;
import io.bootique.jersey.JerseyModule;
import io.bootique.jetty.test.junit.JettyTestFactory;
import io.bootique.mvc.MvcModule;
import io.bootique.mvc.mustache.MvcMustacheModule;

public class MustacheApp extends JettyTestFactory {

    @Override
    public Builder app(String... args) {

        return super.app(args)
                .modules(JerseyModule.class, MvcModule.class, MvcMustacheModule.class)
                .module(binder -> JerseyModule.extend(binder).addResource(MustacheResource.class));
    }

}
