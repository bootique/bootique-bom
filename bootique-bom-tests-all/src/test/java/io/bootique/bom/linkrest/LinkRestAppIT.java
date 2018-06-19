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

package io.bootique.bom.linkrest;

import io.bootique.bom.linkrest.r1.LrResource1;
import io.bootique.command.CommandOutcome;
import io.bootique.jdbc.tomcat.JdbcTomcatModuleProvider;
import io.bootique.jersey.JerseyModule;
import io.bootique.linkrest.LinkRestModuleProvider;
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

public class LinkRestAppIT {

    @Rule
    public BQTestFactory testFactory = new BQTestFactory();

    private BQTestFactory.Builder appBuilder(String... args) {
        return testFactory.app(args)
                .module(new LinkRestModuleProvider())
                .module(new JdbcTomcatModuleProvider())
                .module(b -> JerseyModule.extend(b).addResource(LrResource1.class));
    }

    @Test
    public void testRun_Help() {

        TestIO io = TestIO.noTrace();

        CommandOutcome outcome = appBuilder("--help").bootLogger(io.getBootLogger()).run();
        assertEquals(0, outcome.getExitCode());

        assertTrue(io.getStdout().contains("--help"));
        assertTrue(io.getStdout().contains("--config"));
    }

    @Test
    public void testRun() {

        appBuilder("--config=src/test/resources/io/bootique/bom/linkrest/test.yml", "--server").run();

        WebTarget base = ClientBuilder.newClient().target("http://localhost:12011/");

        // added as a part of a package
        Response r1 = base.path("/lr/lrservlet/lr1").request().get();
        assertEquals(Status.OK.getStatusCode(), r1.getStatus());
        String expected1 = "{\"data\":[{\"id\":5,\"name\":\"name5\"},{\"id\":6,\"name\":\"name6\"}],\"total\":2}";
        assertEquals(expected1, r1.readEntity(String.class));
    }

}
