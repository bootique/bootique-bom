package io.bootique.bom.jdbc;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import io.bootique.bom.jdbc.JdbcApp;
import org.junit.Before;
import org.junit.Test;

import com.nhl.bootique.command.CommandOutcome;

public class JdbcAppIT {

	private JdbcApp app;

	@Before
	public void before() {
		this.app = new JdbcApp();
	}

	@Test
	public void testRun_Help() {
		CommandOutcome outcome = app.run("--help");
		assertEquals(0, outcome.getExitCode());

		String help = app.getStdout();

		assertTrue(help.contains("--runsql"));
		assertTrue(help.contains("--sql"));
	}

	@Test
	public void testRun_SQL_1() {
		CommandOutcome outcome = app.run("--config=src/test/resources/com/nhl/bootique/bom/jdbc/test.yml", "--runsql",
				"--sql=SELECT * FROM T1");
		assertEquals(0, outcome.getExitCode());
		String data = app.getStdout();

		assertTrue(data.contains("1,aa"));
		assertTrue(data.contains("2,bb"));
	}

	@Test
	public void testRun_SQL_2() {
		CommandOutcome outcome = app.run("--config=src/test/resources/com/nhl/bootique/bom/jdbc/test.yml", "--runsql",
				"--sql=SELECT * FROM T1 WHERE ID = 2");
		assertEquals(0, outcome.getExitCode());
		String data = app.getStdout();

		assertFalse(data.contains("1,aa"));
		assertTrue(data.contains("2,bb"));
	}
}
