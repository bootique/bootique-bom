package io.bootique.bom.linkrest;

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
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class LinkRestAppIT {

    @Rule
    public LinkRestApp app = new LinkRestApp();

    @Test
    public void testRun_Help() {

        TestIO io = TestIO.noTrace();

        BQRuntime runtime = app.app("--help")
                .bootLogger(io.getBootLogger())
                .startupAndWaitCheck()
                // using longer startup timeout .. sometimes this fails on Travis..
                .startupTimeout(8, TimeUnit.SECONDS)
                .start();

        CommandOutcome outcome = app.getOutcome(runtime).get();
        assertEquals(0, outcome.getExitCode());

        assertTrue(io.getStdout().contains("--help"));
        assertTrue(io.getStdout().contains("--config"));
    }

    @Test
    public void testRun() throws InterruptedException, ExecutionException, TimeoutException {

        // using longer startup timeout .. sometimes this fails on Travis..
        app.app("--config=src/test/resources/io/bootique/bom/linkrest/test.yml")
                .startupTimeout(8, TimeUnit.SECONDS)
                .start();

        WebTarget base = ClientBuilder.newClient().target("http://localhost:12011/");

        // added as a part of a package
        Response r1 = base.path("/lr/lrservlet/lr1").request().get();
        assertEquals(Status.OK.getStatusCode(), r1.getStatus());
        String expected1 = "{\"data\":[{\"id\":5,\"name\":\"name5\"},{\"id\":6,\"name\":\"name6\"}],\"total\":2}";
        assertEquals(expected1, r1.readEntity(String.class));
    }

}
