package io.bootique.bom.jdbc;

import io.bootique.BQCoreModule;
import io.bootique.command.CommandOutcome;
import io.bootique.jdbc.tomcat.JdbcTomcatModuleProvider;
import io.bootique.test.TestIO;
import io.bootique.test.junit.BQTestFactory;
import org.junit.Rule;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class JdbcAppIT {

	@Rule
	public BQTestFactory testFactory = new BQTestFactory();

	private BQTestFactory.Builder appBuilder(String... args) {
		return testFactory.app(args)
				.module(new JdbcTomcatModuleProvider())
				.module(b -> BQCoreModule.extend(b).addCommand(RunSQLCommand.class));
	}

	@Test
	public void testRun_Help() {

		TestIO io = TestIO.noTrace();
		CommandOutcome outcome = appBuilder("--help").bootLogger(io.getBootLogger()).run();
		assertEquals(0, outcome.getExitCode());

		String help = io.getStdout();
		assertTrue(help.contains("--run-sql"));
		assertTrue(help.contains("--sql"));
	}

	@Test
	public void testRun_SQL_1() {

        TestIO io = TestIO.noTrace();
        CommandOutcome outcome = appBuilder(
                "--config=src/test/resources/io/bootique/bom/jdbc/test.yml",
                "--run-sql",
				"--sql=SELECT * FROM T1")
                .bootLogger(io.getBootLogger())
                .run();

		assertEquals(0, outcome.getExitCode());
		String data = io.getStdout();

		assertTrue(data.contains("1,aa"));
		assertTrue(data.contains("2,bb"));
	}

	@Test
	public void testRun_SQL_2() {

        TestIO io = TestIO.noTrace();

		CommandOutcome outcome = appBuilder(
		        "--config=src/test/resources/io/bootique/bom/jdbc/test.yml",
                "--run-sql",
				"--sql=SELECT * FROM T1 WHERE ID = 2")
                .bootLogger(io.getBootLogger())
                .run();

		assertEquals(0, outcome.getExitCode());
		String data = io.getStdout();

		assertFalse(data.contains("1,aa"));
		assertTrue(data.contains("2,bb"));
	}
}
