package io.bootique.bom.jersey;

import io.bootique.bom.jersey.r1.Resource1;
import io.bootique.bom.jersey.r2.Resource2;
import io.bootique.jersey.JerseyModule;
import io.bootique.jetty.test.junit.JettyTestFactory;

public class JerseyApp extends JettyTestFactory {

    @Override
    public Builder app(String... args) {

        return super.app(args).modules(JerseyModule.class).module((binder) -> {
            JerseyModule.contributeFeatures(binder).addBinding().to(JerseyAppFeature.class);
            JerseyModule.contributePackages(binder).addBinding().toInstance(Resource1.class.getPackage());
            JerseyModule.contributeResources(binder).addBinding().to(Resource2.class);
        });
    }
}
