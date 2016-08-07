package com.nhl.bootique.bom.logback;

import java.util.function.Consumer;

import com.google.inject.Singleton;
import com.nhl.bootique.BQCoreModule;
import com.nhl.bootique.Bootique;
import com.nhl.bootique.annotation.DefaultCommand;
import com.nhl.bootique.command.Command;
import com.nhl.bootique.logback.LogbackModule;
import io.bootique.test.junit.BQTestFactory;

public class LogbackApp extends BQTestFactory {

	@Override
	public Builder newRuntime() {
		Consumer<Bootique> config = (bootique) -> {
			bootique.modules(LogbackModule.class).override(BQCoreModule.class).with((binder) -> {
				binder.bind(Command.class).annotatedWith(DefaultCommand.class).to(LogbackTestCommand.class)
						.in(Singleton.class);
			});
		};

		return super.newRuntime().configurator(config);
	}
}
