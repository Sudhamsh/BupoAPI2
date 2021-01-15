package com.reit.api;

import java.util.Set;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.bson.types.ObjectId;

import com.bupo.util.LogManager;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.reit.services.PropertyService;

@Path("/reit/tag")
public class TagsApi {
	private LogManager logger = LogManager.getLogger(this.getClass());
	private PropertyService propService = new PropertyService();
	private Gson gson = new GsonBuilder().serializeSpecialFloatingPointValues().setDateFormat("yyyy-MM-dd").create();

	@POST
	@Consumes({ MediaType.APPLICATION_JSON })
	@Path("/propertyId/{propertyId}/tag/{tag}")
	public Response addTag(@PathParam("propertyId") String propertyId, @PathParam("tag") String tag) {
		Response response = null;

		try {
			propService.addTag(new ObjectId(propertyId), tag);
			response = Response.status(200).build();
		} catch (Exception e) {
			e.printStackTrace();
			response = Response.serverError().entity(e.getMessage()).build();
		}

		return response;

	}

	@DELETE
	@Consumes({ MediaType.APPLICATION_JSON })
	@Path("/propertyId/{propertyId}/tag/{tag}")
	public Response removeTag(@PathParam("propertyId") String propertyId, @PathParam("tag") String tag) {
		Response response = null;

		try {
			propService.removeTag(new ObjectId(propertyId), tag);
			response = Response.status(200).build();
		} catch (Exception e) {
			e.printStackTrace();
			response = Response.serverError().entity(e.getMessage()).build();
		}

		return response;

	}

	@GET
	@Consumes({ MediaType.APPLICATION_JSON })
	@Path("/propertyId/{propertyId}/")
	public Response getTag(@PathParam("propertyId") String propertyId) {
		Response response = null;

		try {
			Set<String> tags = propService.getTags(new ObjectId(propertyId));
			response = Response.status(200).entity(gson.toJson(tags)).build();
		} catch (Exception e) {
			e.printStackTrace();
			response = Response.serverError().entity(e.getMessage()).build();
		}

		return response;

	}

}