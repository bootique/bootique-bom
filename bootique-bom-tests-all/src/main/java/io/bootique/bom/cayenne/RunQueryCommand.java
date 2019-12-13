/*
 * Licensed to ObjectStyle LLC under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ObjectStyle LLC licenses
 * this file to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package io.bootique.bom.cayenne;

import javax.inject.Inject;
import javax.inject.Provider;

import io.bootique.meta.application.CommandMetadata;
import io.bootique.meta.application.OptionMetadata;
import io.bootique.cli.Cli;
import io.bootique.command.CommandOutcome;
import io.bootique.command.CommandWithMetadata;
import io.bootique.log.BootLogger;
import org.apache.cayenne.CayenneDataObject;
import org.apache.cayenne.DataObject;
import org.apache.cayenne.ObjectContext;
import org.apache.cayenne.ObjectId;
import org.apache.cayenne.configuration.server.ServerRuntime;
import org.apache.cayenne.exp.Expression;
import org.apache.cayenne.exp.ExpressionFactory;
import org.apache.cayenne.query.ObjectSelect;
import org.apache.cayenne.query.SQLTemplate;

public class RunQueryCommand extends CommandWithMetadata {

	private static final String KEY_OPTION = "key";
	private static final String VALUE_OPTION = "value";

	@Inject
	private BootLogger logger;

	@Inject
	private Provider<ServerRuntime> cayenneProvider;

	public RunQueryCommand() {
		super(CommandMetadata.builder(RunQueryCommand.class)
				.addOption(OptionMetadata.builder(KEY_OPTION).valueRequired("property_name"))
				.addOption(OptionMetadata.builder(VALUE_OPTION).valueRequired("property_value")));
	}

	@Override
	public CommandOutcome run(Cli cli) {

		prepareDB();

		ObjectContext context = cayenneProvider.get().newContext();

		String key = cli.optionString(KEY_OPTION);
		String value = cli.optionString(VALUE_OPTION);

		Expression filter = createFilter(key, value);

		ObjectSelect.query(DataObject.class, "T1").where(filter).select(context)
				.forEach(o -> logger.stdout(String.format("(%s): %s", filter, o.readProperty("name"))));

		return CommandOutcome.succeeded();
	}

	private Expression createFilter(String key, String value) {
		return key == null ? null : ExpressionFactory.matchExp(key, value);
	}

	private void prepareDB() {
		ObjectContext context = cayenneProvider.get().newContext();
		
		context.performGenericQuery(new SQLTemplate("T1", "delete from T1"));

		for (int i = 0; i < 10; i++) {

			DataObject o = new CayenneDataObject();
			o.setObjectId(new ObjectId("T1"));
			o.writeProperty("name", "n" + i);

			context.registerNewObject(o);
		}

		context.commitChanges();
	}

}
