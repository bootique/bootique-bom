package io.bootique.bom.cayenne;

import io.bootique.command.CommandOutcome;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class CayenneAppIT {
	private CayenneApp app;

	@Before
	public void before() {
		this.app = new CayenneApp();
	}

	@Test
	public void testRun_Help() {
		CommandOutcome outcome = app.run("--help");
		assertEquals(0, outcome.getExitCode());

		String help = app.getStdout();

		assertTrue(help.contains("--run-query"));
		assertTrue(help.contains("--key"));
		assertTrue(help.contains("--value"));
		assertTrue(help.contains("--config"));
	}

	@Test
	public void testRun_Query() {
		CommandOutcome outcome = app.run("--config=src/test/resources/io/bootique/bom/cayenne/test.yml",
				"--run-query", "--key=name", "--value=n5");
		assertEquals(0, outcome.getExitCode());
		String data = app.getStdout();

		assertTrue(data.contains("n5"));
	}
}
