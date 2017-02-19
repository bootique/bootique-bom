package io.bootique.bom.jdbc;

import com.google.inject.Binder;
import com.google.inject.Module;
import io.bootique.BQCoreModule;
import io.bootique.Bootique;
import io.bootique.bom.BomTestApp;
import io.bootique.jdbc.JdbcModule;

public class JdbcApp extends BomTestApp implements Module {

	@Override
	protected void configure(Bootique bootique) {
		bootique.module(JdbcModule.class).module(this);
	}

	@Override
	public void configure(Binder binder) {
		BQCoreModule.extend(binder).addCommand(RunSQLCommand.class);
	}
}
