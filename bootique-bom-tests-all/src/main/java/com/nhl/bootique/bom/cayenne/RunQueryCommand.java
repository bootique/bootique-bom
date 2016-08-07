package com.nhl.bootique.bom.cayenne;

import org.apache.cayenne.CayenneDataObject;
import org.apache.cayenne.DataObject;
import org.apache.cayenne.ObjectContext;
import org.apache.cayenne.ObjectId;
import org.apache.cayenne.configuration.server.ServerRuntime;
import org.apache.cayenne.exp.Expression;
import org.apache.cayenne.exp.ExpressionFactory;
import org.apache.cayenne.query.ObjectSelect;
import org.apache.cayenne.query.SQLTemplate;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.nhl.bootique.cli.Cli;
import com.nhl.bootique.cli.CliOption;
import com.nhl.bootique.command.CommandMetadata;
import com.nhl.bootique.command.CommandOutcome;
import com.nhl.bootique.command.CommandWithMetadata;
import com.nhl.bootique.log.BootLogger;

public class RunQueryCommand extends CommandWithMetadata {

	private static final String KEY_OPTION = "key";
	private static final String VALUE_OPTION = "value";

	@Inject
	private BootLogger logger;

	@Inject
	private Provider<ServerRuntime> cayenneProvider;

	public RunQueryCommand() {
		super(CommandMetadata.builder(RunQueryCommand.class)
				.addOption(CliOption.builder(KEY_OPTION).valueRequired("property_name"))
				.addOption(CliOption.builder(VALUE_OPTION).valueRequired("property_value")));
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
