package io.bootique.bom.logback;

import com.google.inject.Singleton;
import io.bootique.BQCoreModule;
import io.bootique.annotation.DefaultCommand;
import io.bootique.command.Command;
import io.bootique.logback.LogbackModule;
import io.bootique.test.junit.BQTestFactory;

public class LogbackApp extends BQTestFactory {

    @Override
    public Builder app(String... args) {
        return super.app(args).modules(LogbackModule.class).override(BQCoreModule.class).with((binder) -> {
            binder.bind(Command.class).annotatedWith(DefaultCommand.class).to(LogbackTestCommand.class)
                    .in(Singleton.class);
        });
    }
}
