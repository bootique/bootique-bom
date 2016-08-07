package io.bootique.bom.jetty;

import io.bootique.Bootique;
import io.bootique.jetty.JettyModule;
import io.bootique.jetty.test.junit.JettyTestFactory;

import java.util.function.Consumer;

public class JettyApp extends JettyTestFactory {

	@Override
	public Builder newRuntime() {

		Consumer<Bootique> config = (bootique) -> {
			bootique.module((binder) -> {
				JettyModule.contributeServlets(binder).addBinding().to(BomServlet.class);
				JettyModule.contributeFilters(binder).addBinding().to(BomFilter.class);
			});
		};

		return super.newRuntime().configurator(config);
	}
}
