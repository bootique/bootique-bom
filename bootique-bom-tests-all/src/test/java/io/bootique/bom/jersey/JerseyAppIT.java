package io.bootique.bom.jersey;

import io.bootique.command.CommandOutcome;
import io.bootique.test.BQDaemonTestRuntime;
import org.junit.Rule;
import org.junit.Test;

import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class JerseyAppIT {

	@Rule
	public JerseyApp app = new JerseyApp();

	@Test
	public void testRun_NoCommand() {

		BQDaemonTestRuntime runtime = app.newRuntime().startupAndWaitCheck().start();

		CommandOutcome outcome = runtime.getOutcome().get();
		assertEquals(0, outcome.getExitCode());

		String help = runtime.getStdout();

		assertTrue(help.contains("--server"));
		assertTrue(help.contains("--help"));
		assertTrue(help.contains("--config"));
	}

	@Test
	public void testRun_Help() {
		BQDaemonTestRuntime runtime = app.newRuntime().startupAndWaitCheck().start("--help");

		CommandOutcome outcome = runtime.getOutcome().get();
		assertEquals(0, outcome.getExitCode());

		String help = runtime.getStdout();

		assertTrue(help.contains("--server"));
		assertTrue(help.contains("--help"));
		assertTrue(help.contains("--config"));
	}

	@Test
	public void testRun() throws InterruptedException, ExecutionException, TimeoutException {

		app.newRuntime().startServer("--config=src/test/resources/io/bootique/bom/jersey/test.yml");

		WebTarget base = ClientBuilder.newClient().target("http://localhost:12009/");

		// added as a part of a package
		Response r1 = base.path("/jt/jr/1").request().get();
		assertEquals(Status.OK.getStatusCode(), r1.getStatus());
		String expected1 = "bootique-jersey-resource1:--server,--config=src/test/resources/io/bootique/bom/jersey/test.yml";
		assertEquals(expected1, r1.readEntity(String.class));

		// added as individual resource
		Response r2 = base.path("/jt/jr/2").request().get();
		assertEquals(Status.OK.getStatusCode(), r2.getStatus());
		String expected2 = "bootique-jersey-resource2:--server,--config=src/test/resources/io/bootique/bom/jersey/test.yml";
		assertEquals(expected2, r2.readEntity(String.class));
	}

}
