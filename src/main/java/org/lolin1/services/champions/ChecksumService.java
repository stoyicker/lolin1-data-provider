package org.lolin1.services.champions;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

import org.lolin1.control.Controller;
import org.lolin1.data.DataAccessObject;
import org.lolin1.utils.Utils;

@Path("/champions/imageChecksum/{type}/{name}")
@Produces("application/json")
public class ChecksumService {

	@GET
	public final String get(@PathParam("type") String type,
			@PathParam("name") String name) {
		int imageType;
		switch (type.toUpperCase()) {
		case "BUST":
			imageType = Controller.IMAGE_TYPE_BUST;
			break;
		case "PASSIVE":
			imageType = Controller.IMAGE_TYPE_PASSIVE;
			break;
		case "SPELL":
			imageType = Controller.IMAGE_TYPE_SPELL;
			break;
		default:
			return DataAccessObject.getResponseError();
		}

		return Utils.toSystemJSON("checksum", Controller.getController()
				.getImageHash(imageType, name));
	}
}
