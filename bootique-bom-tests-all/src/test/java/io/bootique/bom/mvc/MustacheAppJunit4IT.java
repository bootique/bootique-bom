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

package io.bootique.bom.mvc;

import io.bootique.bom.mvc.r1.MustacheResource;
import io.bootique.jersey.JerseyModule;
import io.bootique.jetty.JettyModule;
import io.bootique.mvc.MvcModule;
import io.bootique.mvc.mustache.MvcMustacheModule;
import io.bootique.test.junit.BQTestFactory;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.WebTarget;
import jakarta.ws.rs.core.Response;
import org.junit.Rule;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class MustacheAppJunit4IT {

    @Rule
    public BQTestFactory testFactory = new BQTestFactory();

    @Test
    public void run() {

        testFactory.app("--config=src/test/resources/io/bootique/bom/mvc/test.yml", "--server")
                .modules(MvcMustacheModule.class, JerseyModule.class, JettyModule.class, MvcModule.class)
                .module(binder -> JerseyModule.extend(binder).addResource(MustacheResource.class))
                .run();

        WebTarget base = ClientBuilder.newClient().target("http://localhost:8080/");

        // added as a part of a package
        Response r1 = base.path("/mustache").request().get();
        assertEquals(Response.Status.OK.getStatusCode(), r1.getStatus());
        assertEquals("hi_myname.", r1.readEntity(String.class));
    }

}
