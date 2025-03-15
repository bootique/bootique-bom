/*
 * Licensed to ObjectStyle LLC under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ObjectStyle LLC licenses
 * this file to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package io.bootique.bom.jetty;

import io.bootique.jetty.JettyModule;
import io.bootique.test.junit.BQTestFactory;
import io.bootique.test.junit.TestIO;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.WebTarget;
import jakarta.ws.rs.core.Response;
import org.junit.Rule;
import org.junit.Test;

import static org.junit.Assert.*;

public class JettyAppJunit4IT {

    @Rule
    public BQTestFactory testFactory = new BQTestFactory();

    private BQTestFactory.Builder appBuilder(String... args) {
        return testFactory.app(args)
                .modules(JettyModule.class)
                .module(b -> JettyModule.extend(b).addServlet(BomServlet.class).addFilter(BomFilter.class));
    }

    @Test
    public void run_Help() {

        TestIO io = TestIO.noTrace();
        appBuilder("--help").bootLogger(io.getBootLogger()).run();

        assertTrue(io.getStdout().contains("--help"));
        assertTrue(io.getStdout().contains("--config"));
    }

    @Test
    public void run() {

        appBuilder("--config=src/test/resources/io/bootique/bom/jetty/test.yml", "--server").run();

        WebTarget base = ClientBuilder.newClient().target("http://localhost:11234/");

        Response r1 = base.path("/testc").request().get();
        assertEquals(Response.Status.OK.getStatusCode(), r1.getStatus());
        String expected1 = String.format("bom_filter_before%nbom_servlet_query_string: null%nbom_filter_after%n");
        assertEquals(expected1, r1.readEntity(String.class));

        Response r2 = base.path("/testb").request().get();
        assertEquals(Response.Status.NOT_FOUND.getStatusCode(), r2.getStatus());

        Response r3 = base.path("/testc").queryParam("p", "v").request().get();
        assertEquals(Response.Status.OK.getStatusCode(), r3.getStatus());
        String expected3 = String.format("bom_filter_before%nbom_servlet_query_string: p=v%nbom_filter_after%n");
        assertEquals(expected3, r3.readEntity(String.class));
    }
}
