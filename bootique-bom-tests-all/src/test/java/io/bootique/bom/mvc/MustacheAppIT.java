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

import io.bootique.BQRuntime;
import io.bootique.Bootique;
import io.bootique.bom.mvc.r1.MustacheResource;
import io.bootique.jersey.JerseyModule;
import io.bootique.jetty.JettyModule;
import io.bootique.jetty.junit5.JettyTester;
import io.bootique.junit5.BQApp;
import io.bootique.junit5.BQTest;
import io.bootique.mvc.MvcModule;
import io.bootique.mvc.mustache.MvcMustacheModule;
import org.junit.jupiter.api.Test;

import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import static org.junit.jupiter.api.Assertions.assertEquals;

@BQTest
public class MustacheAppIT {

    static final JettyTester jetty = JettyTester.create();

    @BQApp
    final BQRuntime app = Bootique.app("-c", "classpath:io/bootique/bom/mvc/test.yml", "-s")
            .modules(MvcMustacheModule.class, JerseyModule.class, JettyModule.class, MvcModule.class)
            .module(b -> JerseyModule.extend(b).addResource(MustacheResource.class))
            .module(jetty.moduleReplacingConnectors())
            .createRuntime();

    @Test
    public void run() {

        WebTarget base = jetty.getTarget();

        Response r1 = base.path("/mustache").request().get();
        assertEquals(Status.OK.getStatusCode(), r1.getStatus());
        assertEquals("hi_myname.", r1.readEntity(String.class));
    }

}
