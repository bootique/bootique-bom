package com.nhl.bootique.bom.jersey;

import java.util.function.Consumer;

import com.nhl.bootique.Bootique;
import com.nhl.bootique.bom.jersey.r1.Resource1;
import com.nhl.bootique.bom.jersey.r2.Resource2;
import com.nhl.bootique.jersey.JerseyModule;
import com.nhl.bootique.jetty.test.junit.JettyTestFactory;

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
