/**
 *    Licensed to the ObjectStyle LLC under one
 *  or more contributor license agreements.  See the NOTICE file
 *  distributed with this work for additional information
 *  regarding copyright ownership.  The ObjectStyle LLC licenses
 *  this file to you under the Apache License, Version 2.0 (the
 *  “License”); you may not use this file except in compliance
 *  with the License.  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing,
 *  software distributed under the License is distributed on an
 *  “AS IS” BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 *  KIND, either express or implied.  See the License for the
 *  specific language governing permissions and limitations
 *  under the License.
 */

package io.bootique.bom.logback;

import io.bootique.meta.application.CommandMetadata;
import io.bootique.cli.Cli;
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
