package io.bootique.bom.swagger.api;

import io.swagger.annotations.Api;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;

@Api
@Path("/api1")
public class Api1 {

    @GET
    @Path("{id}")
    public String getById(@PathParam("id") int id) {
       return "Hi " + id;
    }
}
