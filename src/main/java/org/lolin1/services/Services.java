package org.lolin1.services;

import java.io.File;

import javax.activation.MimetypesFileTypeMap;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

import org.lolin1.control.Controller;
import org.lolin1.data.DataAccessObject;

public final class Services {

	@Path("/champions/getChampions")
	@GET
	@Produces("application/json")
	public final String getChampions(@PathParam("realm") String realm,
			@PathParam("locale") String locale) {
		return DataAccessObject.getJSONChampions(realm, locale);
	}

	@Path("/champions/getVersion")
	@GET
	@Produces("application/json")
	public final String getChampionsFileVersion(@PathParam("realm") String realm) {
		return DataAccessObject.getJSONVersion(
				DataAccessObject.getVersionKeyChampions(), realm);
	}

	@Path("/champions/getImage")
	@GET
	@Produces("image/png")
	public final Response getImage(@PathParam("realm") String realm,
			@PathParam("type") String type, @PathParam("name") String name) {
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
			return Response.status(404).build();
		}
		File img = Controller.getController().getImage(realm, imageType, name);
		return Response.ok(img, new MimetypesFileTypeMap().getContentType(img))
				.build();
	}

	// TODO Make it work

	@Path("/champions/getImageMD5")
	@GET
	@Produces("text/plain")
	public final String getImageMD5(@PathParam("type") String type,
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
		return Controller.getController().getImageHash(imageType, name);
	}
}
