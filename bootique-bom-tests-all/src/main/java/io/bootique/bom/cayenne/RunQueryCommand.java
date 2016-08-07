package io.bootique.bom.cayenne;

import com.google.inject.Inject;
import com.google.inject.Provider;
import io.bootique.cli.Cli;
import io.bootique.cli.CliOption;
import io.bootique.command.CommandMetadata;
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
