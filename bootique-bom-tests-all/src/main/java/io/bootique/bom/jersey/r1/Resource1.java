package io.bootique.bom.jersey.r1;

import com.google.inject.Inject;
import io.bootique.annotation.Args;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.Arrays;

import static java.util.stream.Collectors.joining;

@Path("/1")
@Produces(MediaType.TEXT_PLAIN)
public class Resource1 {

	@Inject
	@Args
	private String[] args;

	@GET
	public String get() {
		String argString = Arrays.asList(args).stream().collect(joining(","));
		return "bootique-jersey-resource1:" + argString;
	}
}
