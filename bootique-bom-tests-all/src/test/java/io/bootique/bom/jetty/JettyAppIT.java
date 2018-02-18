package io.bootique.bom.jetty;

import io.bootique.jetty.JettyModule;
import io.bootique.jetty.JettyModuleProvider;
import io.bootique.test.TestIO;
import io.bootique.test.junit.BQTestFactory;
import org.junit.Rule;
import org.junit.Test;

import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class JettyAppIT {

    @Rule
    public BQTestFactory testFactory = new BQTestFactory();

    private BQTestFactory.Builder appBuilder(String... args) {
        return testFactory.app(args)
                .module(new JettyModuleProvider())
                .module(b -> JettyModule.extend(b).addServlet(BomServlet.class).addFilter(BomFilter.class));
    }

    @Test
    public void testRun_Help() {

        TestIO io = TestIO.noTrace();
        appBuilder("--help").bootLogger(io.getBootLogger()).run();

        assertTrue(io.getStdout().contains("--help"));
        assertTrue(io.getStdout().contains("--config"));
    }

    @Test
    public void testRun() throws InterruptedException {

        appBuilder("--config=src/test/resources/io/bootique/bom/jetty/test.yml", "--server").run();

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
