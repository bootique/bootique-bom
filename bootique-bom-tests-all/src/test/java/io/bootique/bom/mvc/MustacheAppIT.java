package io.bootique.bom.mvc;

import static org.junit.Assert.assertEquals;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.junit.Rule;
import org.junit.Test;

public class MustacheAppIT {

	@Rule
	public MustacheApp app = new MustacheApp();

	@Test
	public void testRun() throws InterruptedException, ExecutionException, TimeoutException {

		app.newRuntime().startServer("--config=src/test/resources/com/nhl/bootique/bom/mvc/test.yml");

		WebTarget base = ClientBuilder.newClient().target("http://localhost:8080/");

		// added as a part of a package
		Response r1 = base.path("/mustache").request().get();
		assertEquals(Status.OK.getStatusCode(), r1.getStatus());
		assertEquals("hi_myname.", r1.readEntity(String.class));
	}

}
