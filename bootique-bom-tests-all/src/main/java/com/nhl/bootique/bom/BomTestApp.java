package com.nhl.bootique.bom;

import com.nhl.bootique.BQRuntime;
import com.nhl.bootique.Bootique;
import com.nhl.bootique.command.CommandOutcome;
import com.nhl.bootique.log.BootLogger;
import com.nhl.bootique.log.DefaultBootLogger;

/**
 * A base app class for BOM integration tests.
 */
public abstract class BomTestApp {

	private InMemoryPrintStream stdout;
	private InMemoryPrintStream stderr;

	public BomTestApp() {
		this.stdout = new InMemoryPrintStream(System.out);
		this.stderr = new InMemoryPrintStream(System.err);
	}

	public CommandOutcome run(String... args) {

		BootLogger logger = createBootLogger();
		Bootique bootique = Bootique.app(args).bootLogger(logger);
		configure(bootique);

		BQRuntime runtime = bootique.createRuntime();
		try {
			return runtime.getRunner().run();
		} catch (Exception e) {
			logger.stderr("Error", e);
			return CommandOutcome.failed(1, getStderr());
		} finally {
			runtime.shutdown();
		}
	}

	protected abstract void configure(Bootique bootique);

	protected BootLogger createBootLogger() {
		return new DefaultBootLogger(true, stdout, stderr);
	}

	public String getStdout() {
		return stdout.toString();
	}

	public String getStderr() {
		return stderr.toString();
	}
}
