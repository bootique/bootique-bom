package com.nhl.bootique.bom.cayenne;

import com.google.inject.Binder;
import com.google.inject.Module;
import com.nhl.bootique.BQCoreModule;
import com.nhl.bootique.Bootique;
import com.nhl.bootique.bom.BomTestApp;
import com.nhl.bootique.cayenne.CayenneModule;
import com.nhl.bootique.jdbc.JdbcModule;

public class CayenneApp extends BomTestApp implements Module {

	@Override
	protected void configure(Bootique bootique) {
		bootique.modules(JdbcModule.class, CayenneModule.class).module(this);
	}

	@Override
	public void configure(Binder binder) {
		BQCoreModule.contributeCommands(binder).addBinding().to(RunQueryCommand.class);
	}
}
