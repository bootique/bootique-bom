package io.bootique.bom.mvc;

import io.bootique.Bootique;
import io.bootique.bom.mvc.r1.MustacheResource;
import io.bootique.jersey.JerseyModule;
import io.bootique.jetty.test.junit.JettyTestFactory;
import io.bootique.mvc.MvcModule;
import io.bootique.mvc.mustache.MvcMustacheModule;

import java.util.function.Consumer;

public class MustacheApp extends JettyTestFactory {

	@Override
	public Builder newRuntime() {
		
		Consumer<Bootique> config = (bootique) -> {
			bootique.modules(JerseyModule.class, MvcModule.class, MvcMustacheModule.class).module((binder) -> {
				JerseyModule.contributeResources(binder).addBinding().to(MustacheResource.class);
			});
		};

		return super.newRuntime().configurator(config);
	}

}
