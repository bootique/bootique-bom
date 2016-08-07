package io.bootique.bom.jersey.r2;

import static java.util.stream.Collectors.joining;

import java.util.Arrays;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.google.inject.Inject;
import com.nhl.bootique.annotation.Args;

@Path("/2")
@Produces(MediaType.TEXT_PLAIN)
public class Resource2 {

	@Inject
	@Args
	private String[] args;

	@GET
	public String get() {
		String argString = Arrays.asList(args).stream().collect(joining(","));
		return "bootique-jersey-resource2:" + argString;
	}
}