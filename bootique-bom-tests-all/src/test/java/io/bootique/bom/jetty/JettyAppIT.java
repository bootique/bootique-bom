package io.bootique.bom.jetty;

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

public class JettyAppIT {

	@Rule
	public JettyApp app = new JettyApp();

	@Test
	public void testRun_Help() throws InterruptedException {

		TestIO io = TestIO.noTrace();
		app.app("--help").bootLogger(io.getBootLogger()).startupAndWaitCheck().start();

		assertTrue(io.getStdout().contains("--help"));
		assertTrue(io.getStdout().contains("--config"));
	}

	@Test
	public void testRun() throws InterruptedException, ExecutionException, TimeoutException {

		app.app("--config=src/test/resources/io/bootique/bom/jetty/test.yml").start();

		// wait for Jetty to start and run some web requests...
		Thread.sleep(1000);
		WebTarget base = ClientBuilder.newClient().target("http://localhost:11234/");

		Response r1 = base.path("/testc").request().get();
		assertEquals(Status.OK.getStatusCode(), r1.getStatus());
		String expected1 = String.format("bom_filter_before%nbom_servlet_query_string: null%nbom_filter_after%n");
		assertEquals(expected1, r1.readEntity(String.class));

		Response r2 = base.path("/testb").request().get();
		assertEquals(Status.NOT_FOUND.getStatusCode(), r2.getStatus());

		Response r3 = base.path("/testc").queryParam("p", "v").request().get();
		assertEquals(Status.OK.getStatusCode(), r3.getStatus());
		String expected3 = String.format("bom_filter_before%nbom_servlet_query_string: p=v%nbom_filter_after%n");
		assertEquals(expected3, r3.readEntity(String.class));
	}
}
