package io.bootique.bom.cayenne;

import com.google.inject.Binder;
import com.google.inject.Module;
import io.bootique.BQCoreModule;
import io.bootique.Bootique;
import io.bootique.bom.BomTestApp;
import io.bootique.cayenne.CayenneModule;
import io.bootique.jdbc.JdbcModule;

public class CayenneApp extends BomTestApp implements Module {

	@Override
	protected void configure(Bootique bootique) {
		bootique.modules(JdbcModule.class, CayenneModule.class).module(this);
	}

	@Override
	public void configure(Binder binder) {
		BQCoreModule.extend(binder).addCommand(RunQueryCommand.class);
	}
}
