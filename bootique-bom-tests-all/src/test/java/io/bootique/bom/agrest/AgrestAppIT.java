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

package io.bootique.bom.agrest;

import io.bootique.agrest.v5.AgrestModule;
import io.bootique.bom.agrest.r1.AgResource1;
import io.bootique.cayenne.v42.CayenneModule;
import io.bootique.command.CommandOutcome;
import io.bootique.jdbc.JdbcModule;
import io.bootique.jdbc.hikaricp.JdbcHikariCPModule;
import io.bootique.jdbc.junit5.derby.DerbyTester;
import io.bootique.jersey.JerseyModule;
import io.bootique.jetty.JettyModule;
import io.bootique.junit5.*;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.WebTarget;
import jakarta.ws.rs.core.Response;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@BQTest
public class AgrestAppIT {

    @BQTestTool
    final DerbyTester db = DerbyTester.db();

    @BQTestTool
    final BQTestFactory testFactory = new BQTestFactory();

    private TestRuntumeBuilder appBuilder(String... args) {
        return testFactory.app(args)
                .module(db.moduleWithTestDataSource("ds"))
                .modules(AgrestModule.class, JdbcHikariCPModule.class, JdbcModule.class, CayenneModule.class, JettyModule.class, JerseyModule.class)
                .module(b -> JerseyModule.extend(b).addResource(AgResource1.class));
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

        appBuilder("--config=src/test/resources/io/bootique/bom/agrest/test.yml", "--server").run();

        WebTarget base = ClientBuilder.newClient().target("http://localhost:12011/");

        // added as a part of a package
        Response r1 = base.path("/lr/lrservlet/lr1").request().get();
        assertEquals(Response.Status.OK.getStatusCode(), r1.getStatus());
        String expected1 = "{\"data\":[{\"id\":5,\"name\":\"name5\"},{\"id\":6,\"name\":\"name6\"}],\"total\":2}";
        assertEquals(expected1, r1.readEntity(String.class));
    }

}
