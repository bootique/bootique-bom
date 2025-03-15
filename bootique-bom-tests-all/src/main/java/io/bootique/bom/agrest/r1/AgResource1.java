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

package io.bootique.bom.agrest.r1;

import io.agrest.DataResponse;
import io.agrest.jaxrs3.AgJaxrs;
import io.agrest.meta.AgEntity;
import io.agrest.meta.AgEntityOverlay;
import io.bootique.bom.agrest.ITEntity;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Configuration;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.UriInfo;

import java.util.List;

@Path("/lr1")
@Produces(MediaType.APPLICATION_JSON)
public class AgResource1 {

    @Context
    private Configuration config;

    @GET
    public DataResponse<ITEntity> get(@Context UriInfo info) {

        AgEntityOverlay<ITEntity> overlay = AgEntity.overlay(ITEntity.class)
                .dataResolver(c -> {
                    ITEntity e1 = new ITEntity();
                    e1.setId(5);
                    e1.setName("name5");

                    ITEntity e2 = new ITEntity();
                    e2.setId(6);
                    e2.setName("name6");

                    return List.of(e1, e2);
                });

        return AgJaxrs.select(ITEntity.class, config)
                .clientParams(info.getQueryParameters())
                .entityOverlay(overlay)
                .get();
    }
}
