package io.bootique.bom.mvc;

import org.junit.Rule;
import org.junit.Test;

import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

import static org.junit.Assert.assertEquals;

public class MustacheAppIT {

	@Rule
	public MustacheApp app = new MustacheApp();

	@Test
	public void testRun() throws InterruptedException, ExecutionException, TimeoutException {

		app.app("--config=src/test/resources/io/bootique/bom/mvc/test.yml").startServer();

		WebTarget base = ClientBuilder.newClient().target("http://localhost:8080/");

		// added as a part of a package
		Response r1 = base.path("/mustache").request().get();
		assertEquals(Status.OK.getStatusCode(), r1.getStatus());
		assertEquals("hi_myname.", r1.readEntity(String.class));
	}

}
