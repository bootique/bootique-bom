package io.bootique.bom;

import io.bootique.BQRuntime;
import io.bootique.Bootique;
import io.bootique.command.CommandOutcome;
import io.bootique.log.BootLogger;
import io.bootique.log.DefaultBootLogger;
import io.bootique.run.Runner;
import io.bootique.test.InMemoryPrintStream;

import java.util.function.Consumer;

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
        return run(r -> {
        }, args);
    }

    public CommandOutcome run(Consumer<BQRuntime> beforeShutdownCallback, String... args) {

        BootLogger logger = createBootLogger();
        Bootique bootique = Bootique.app(args).bootLogger(logger);
        configure(bootique);

        BQRuntime runtime = bootique.createRuntime();
        try {
            return runtime.getInstance(Runner.class).run();
        } catch (Exception e) {
            logger.stderr("Error", e);
            return CommandOutcome.failed(1, getStderr());
        } finally {

            try {
                beforeShutdownCallback.accept(runtime);
            }
            finally {
                runtime.shutdown();
            }
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
