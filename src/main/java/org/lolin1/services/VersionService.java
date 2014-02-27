package org.lolin1.services;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

import org.lolin1.data.DataAccessObject;

@Path("/champions/version/{realm}")
@Produces("application/json")
public final class VersionService {

	@GET
	public final String get(@PathParam("realm") String realm) {
		return DataAccessObject.getJSONVersion(realm);
	}
}
