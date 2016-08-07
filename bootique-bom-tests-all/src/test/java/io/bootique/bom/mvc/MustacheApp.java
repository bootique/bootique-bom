package io.bootique.bom.mvc;

import java.util.function.Consumer;

import com.nhl.bootique.Bootique;
import io.bootique.bom.mvc.r1.MustacheResource;
import com.nhl.bootique.jersey.JerseyModule;
import com.nhl.bootique.jetty.test.junit.JettyTestFactory;
import com.nhl.bootique.mvc.MvcModule;
import com.nhl.bootique.mvc.mustache.MvcMustacheModule;

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
