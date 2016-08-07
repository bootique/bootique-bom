package com.nhl.bootique.bom.logback;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.nhl.bootique.cli.Cli;
import com.nhl.bootique.command.CommandMetadata;
import com.nhl.bootique.command.CommandOutcome;
import com.nhl.bootique.command.CommandWithMetadata;

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
