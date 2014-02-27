package org.lolin1.services;

import java.io.File;

import javax.activation.MimetypesFileTypeMap;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.lolin1.control.Controller;
import org.lolin1.data.DataAccessObject;

@Path("/champions/image")
@Produces("image/png")
public class ImagesService {

	@GET
	public final Response get(@PathParam("realm") String realm,
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
		if (img != null) {
			return Response.ok(img,
					new MimetypesFileTypeMap().getContentType(img)).build();
		} else {
			return Response.ok(DataAccessObject.getResponseError(),
					MediaType.APPLICATION_JSON).build();
		}
	}
}
