package org.lolin1.services;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

import org.lolin1.data.DataAccessObject;

@Path("/champions/list/{realm}/{locale}")
@Produces("application/json")
public class ChampionsService {
	@GET
	public final String get(@PathParam("realm") String realm,
			@PathParam("locale") String locale) {
		return DataAccessObject.getJSONChampions(realm, locale);
	}
}
