package io.bootique.bom.cayenne;

import io.bootique.BQCoreModule;
import io.bootique.cayenne.CayenneModuleProvider;
import io.bootique.command.CommandOutcome;
import io.bootique.jdbc.tomcat.TomcatJdbcModule;
import io.bootique.test.TestIO;
import io.bootique.test.junit.BQTestFactory;
import org.junit.Rule;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class CayenneAppIT {

    @Rule
    public BQTestFactory testFactory = new BQTestFactory();

    private BQTestFactory.Builder appBuilder(String... args) {
        return testFactory.app(args)
                .module(new CayenneModuleProvider())
                .module(new TomcatJdbcModule())
                .module(b -> BQCoreModule.extend(b).addCommand(RunQueryCommand.class));
    }

    @Test
    public void testRun_Help() {

        TestIO io = TestIO.noTrace();
        CommandOutcome outcome = appBuilder("--help").bootLogger(io.getBootLogger()).run();
        assertEquals(0, outcome.getExitCode());

        String help = io.getStdout();

        assertTrue(help.contains("--run-query"));
        assertTrue(help.contains("--key"));
        assertTrue(help.contains("--value"));
        assertTrue(help.contains("--config"));
    }

    @Test
    public void testRun_Query() {

        TestIO io = TestIO.noTrace();
        CommandOutcome outcome = appBuilder(
                "--config=src/test/resources/io/bootique/bom/cayenne/test.yml",
                "--run-query",
                "--key=name",
                "--value=n5")
                .bootLogger(io.getBootLogger())
                .run();

        assertEquals(0, outcome.getExitCode());
        assertTrue(io.getStdout().contains("n5"));
    }
}
