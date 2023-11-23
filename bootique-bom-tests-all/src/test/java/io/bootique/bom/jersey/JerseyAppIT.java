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

package io.bootique.bom.jersey;

import io.bootique.bom.jersey.r1.Resource1;
import io.bootique.bom.jersey.r2.Resource2;
import io.bootique.command.CommandOutcome;
import io.bootique.jersey.JerseyModule;
import io.bootique.jersey.JerseyModuleProvider;
import io.bootique.junit5.*;
import org.junit.jupiter.api.Test;


import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;


@BQTest
public class JerseyAppIT {

    @BQTestTool
    final BQTestFactory testFactory = new BQTestFactory();

    private TestRuntumeBuilder appBuilder(String... args) {
        return testFactory.app(args)
                .moduleProvider(new JerseyModuleProvider())
                .module(b -> JerseyModule.extend(b)
                        .addFeature(JerseyAppFeature.class)
                        .addPackage(Resource1.class.getPackage())
                        .addResource(Resource2.class));
    }

    @Test
    public void run_Help() {

        TestIO io = TestIO.noTrace();
        CommandOutcome outcome = appBuilder("--help").bootLogger(io.getBootLogger()).run();
        assertEquals(0, outcome.getExitCode());

        assertTrue(io.getStdout().contains("--help"));
        assertTrue(io.getStdout().contains("--config"));
    }

    @Test
    public void run() {

        appBuilder("--config=src/test/resources/io/bootique/bom/jersey/test.yml", "--server").run();

        WebTarget base = ClientBuilder.newClient().target("http://localhost:12009/");

        // added as a part of a package
        Response r1 = base.path("/jt/jr/1").request().get();
        assertEquals(Status.OK.getStatusCode(), r1.getStatus());
        String expected1 = "bootique-jersey-resource1:--config=src/test/resources/io/bootique/bom/jersey/test.yml,--server";
        assertEquals(expected1, r1.readEntity(String.class));

        // added as individual resource
        Response r2 = base.path("/jt/jr/2").request().get();
        assertEquals(Status.OK.getStatusCode(), r2.getStatus());
        String expected2 = "bootique-jersey-resource2:--config=src/test/resources/io/bootique/bom/jersey/test.yml,--server";
        assertEquals(expected2, r2.readEntity(String.class));
    }

}
