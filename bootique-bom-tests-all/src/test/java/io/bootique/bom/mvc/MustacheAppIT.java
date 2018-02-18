package io.bootique.bom.mvc;

import io.bootique.bom.mvc.r1.MustacheResource;
import io.bootique.jersey.JerseyModule;
import io.bootique.jetty.JettyModule;
import io.bootique.mvc.MvcModule;
import io.bootique.mvc.mustache.MvcMustacheModule;
import io.bootique.test.junit.BQTestFactory;
import org.junit.Rule;
import org.junit.Test;

import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import static org.junit.Assert.assertEquals;

public class MustacheAppIT {

    @Rule
    public BQTestFactory testFactory = new BQTestFactory();

    @Test
    public void testRun() {

        testFactory.app("--config=src/test/resources/io/bootique/bom/mvc/test.yml", "--server")
                .modules(JettyModule.class, JerseyModule.class, MvcModule.class, MvcMustacheModule.class)
                .module(binder -> JerseyModule.extend(binder).addResource(MustacheResource.class))
                .run();

        WebTarget base = ClientBuilder.newClient().target("http://localhost:8080/");

        // added as a part of a package
        Response r1 = base.path("/mustache").request().get();
        assertEquals(Status.OK.getStatusCode(), r1.getStatus());
        assertEquals("hi_myname.", r1.readEntity(String.class));
    }

}
