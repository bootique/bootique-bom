package com.nhl.bootique.bom.jdbc;

import com.google.inject.Binder;
import com.google.inject.Module;
import com.nhl.bootique.BQCoreModule;
import com.nhl.bootique.Bootique;
import com.nhl.bootique.bom.BomTestApp;
import com.nhl.bootique.jdbc.JdbcModule;

public class JdbcApp extends BomTestApp implements Module {

	@Override
	protected void configure(Bootique bootique) {
		bootique.module(JdbcModule.class).module(this);
	}

	@Override
	public void configure(Binder binder) {
		BQCoreModule.contributeCommands(binder).addBinding().to(RunSQLCommand.class);
	}
}
