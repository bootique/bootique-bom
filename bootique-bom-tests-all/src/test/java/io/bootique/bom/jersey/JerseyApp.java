package io.bootique.bom.jersey;

import io.bootique.Bootique;
import io.bootique.bom.jersey.r1.Resource1;
import io.bootique.bom.jersey.r2.Resource2;
import io.bootique.jersey.JerseyModule;
import io.bootique.jetty.test.junit.JettyTestFactory;

import java.util.function.Consumer;

public class JerseyApp extends JettyTestFactory {

	@Override
	public Builder newRuntime() {
		Consumer<Bootique> config = (bootique) -> {
			bootique.modules(JerseyModule.class).module((binder) -> {
				JerseyModule.contributeFeatures(binder).addBinding().to(JerseyAppFeature.class);
				JerseyModule.contributePackages(binder).addBinding().toInstance(Resource1.class.getPackage());
				JerseyModule.contributeResources(binder).addBinding().to(Resource2.class);
			});
		};

		return super.newRuntime().configurator(config);
	}
}
