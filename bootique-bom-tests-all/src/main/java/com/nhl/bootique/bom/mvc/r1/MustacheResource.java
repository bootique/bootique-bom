package com.nhl.bootique.bom.mvc.r1;

import javax.ws.rs.GET;
import javax.ws.rs.Path;

@Path("/mustache")
public class MustacheResource {

	@GET
	public T1View get() {
		return new T1View("myname");
	}
}
