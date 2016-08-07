package com.nhl.bootique.bom.linkrest.r1;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Configuration;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriInfo;

import com.nhl.bootique.bom.linkrest.ITEntity;
import com.nhl.bootique.bom.linkrest.PojoListener;
import com.nhl.link.rest.DataResponse;
import com.nhl.link.rest.LinkRest;

@Path("/lr1")
@Produces(MediaType.APPLICATION_JSON)
public class LrResource1 {

	@Context
	private Configuration config;

	@GET
	public DataResponse<ITEntity> get(UriInfo info) {
		return LinkRest.select(ITEntity.class, config).uri(info).listener(new PojoListener()).select();
	}
}
