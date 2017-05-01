package io.bootique.bom.jersey;

import io.bootique.BQRuntime;
import io.bootique.command.CommandOutcome;
import io.bootique.test.TestIO;
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
	public void testRun_Help() {

		TestIO io = TestIO.noTrace();
		BQRuntime runtime = app.app("--help").bootLogger(io.getBootLogger()).startupAndWaitCheck().start();

		CommandOutcome outcome = app.getOutcome(runtime).get();
		assertEquals(0, outcome.getExitCode());

		assertTrue(io.getStdout().contains("--help"));
		assertTrue(io.getStdout().contains("--config"));
	}

	@Test
	public void testRun() throws InterruptedException, ExecutionException, TimeoutException {

		app.app("--config=src/test/resources/io/bootique/bom/jersey/test.yml").start();

		WebTarget base = ClientBuilder.newClient().target("http://localhost:12009/");

		// added as a part of a package
		Response r1 = base.path("/jt/jr/1").request().get();
		assertEquals(Status.OK.getStatusCode(), r1.getStatus());
		String expected1 = "bootique-jersey-resource1:--config=src/test/resources/io/bootique/bom/jersey/test.yml";
		assertEquals(expected1, r1.readEntity(String.class));

		// added as individual resource
		Response r2 = base.path("/jt/jr/2").request().get();
		assertEquals(Status.OK.getStatusCode(), r2.getStatus());
		String expected2 = "bootique-jersey-resource2:--config=src/test/resources/io/bootique/bom/jersey/test.yml";
		assertEquals(expected2, r2.readEntity(String.class));
	}

}
