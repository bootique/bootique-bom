package io.bootique.bom.jersey;

import javax.ws.rs.core.Feature;
import javax.ws.rs.core.FeatureContext;

public class JerseyAppFeature implements Feature {

	@Override
	public boolean configure(FeatureContext context) {
		// TODO: add something useful...
		return true;
	}
}
