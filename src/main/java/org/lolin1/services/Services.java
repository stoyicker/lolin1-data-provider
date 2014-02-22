package org.lolin1.services;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

import org.lolin1.data.DataAccessObject;

@Path("/champions")
@Produces("application/json")
public final class Services {

	@GET
	public final String getChampions(@PathParam("realm") String realm,
			@PathParam("locale") String locale) {
		return DataAccessObject.getJSONChampions(realm, locale);
	}

	@GET
	public final String getDragonMagicVersion(@PathParam("realm") String realm) {
		return DataAccessObject.getJSONVersion(realm);
	}
}
