package io.bootique.bom.linkrest;

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

public class LinkRestAppIT {

	@Rule
	public LinkRestApp app = new LinkRestApp();

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

		app.newRuntime().startServer("--config=src/test/resources/io/bootique/bom/linkrest/test.yml");

		WebTarget base = ClientBuilder.newClient().target("http://localhost:12011/");

		// added as a part of a package
		Response r1 = base.path("/lr/lrservlet/lr1").request().get();
		assertEquals(Status.OK.getStatusCode(), r1.getStatus());
		String expected1 = "{\"data\":[{\"id\":5,\"name\":\"name5\"},{\"id\":6,\"name\":\"name6\"}],\"total\":2}";
		assertEquals(expected1, r1.readEntity(String.class));
	}

}
