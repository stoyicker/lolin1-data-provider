package org.lolin1.services;

import java.io.IOException;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import org.lolin1.utils.Files;

@Path("/champions")
@Produces("application/json")
public final class ChampionListService {

	private static String RESPONSE_ERROR = "{\"sanity\":\"error\"}";

	@GET
	public String getChampions() {
		try {
			return Files.readFile(Files.CHAMPIONS_FILE);
		} catch (IOException e) {
			return ChampionListService.RESPONSE_ERROR;
		}
	}
}
