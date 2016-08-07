package io.bootique.bom.logback;

import io.bootique.cli.Cli;
import io.bootique.command.CommandMetadata;
import io.bootique.command.CommandOutcome;
import io.bootique.command.CommandWithMetadata;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LogbackTestCommand extends CommandWithMetadata {

	private static final Logger LOGGER = LoggerFactory.getLogger(LogbackTestCommand.class);

	public LogbackTestCommand() {
		super(CommandMetadata.builder(LogbackTestCommand.class));
	}

	@Override
	public CommandOutcome run(Cli cli) {
		LOGGER.debug("logback-test-debug");
		LOGGER.info("logback-test-info");
		LOGGER.warn("logback-test-warn");
		LOGGER.error("logback-test-error");
		return CommandOutcome.succeeded();
	}
}
