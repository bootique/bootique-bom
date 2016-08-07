package io.bootique.bom.logback;

import com.google.inject.Singleton;
import io.bootique.BQCoreModule;
import io.bootique.Bootique;
import io.bootique.annotation.DefaultCommand;
import io.bootique.command.Command;
import io.bootique.logback.LogbackModule;
import io.bootique.test.junit.BQTestFactory;

import java.util.function.Consumer;

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
