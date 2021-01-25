package com.reit.api;

import java.security.Principal;
import java.util.Set;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

import org.bson.types.ObjectId;

import com.bupo.util.LogManager;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.reit.beans.ErrorBean;
import com.reit.services.PropertyService;
import com.reit.util.Secured;

@Path("/reit/tag")
public class TagsApi {
	private LogManager logger = LogManager.getLogger(this.getClass());
	private PropertyService propService = new PropertyService();
	private Gson gson = new GsonBuilder().serializeSpecialFloatingPointValues().setDateFormat("yyyy-MM-dd").create();

	@POST
	@Secured
	@Consumes({ MediaType.APPLICATION_JSON })
	@Path("/propertyId/{propertyId}/tag/{tag}")
	public Response addTag(@PathParam("propertyId") String propertyId, @PathParam("tag") String tag,
			@Context SecurityContext securityContext) {
		Response response = null;

		try {
			Principal principal = securityContext.getUserPrincipal();
			String username = principal.getName();
			System.out.println("username .... " + username);
			propService.addTag(new ObjectId(propertyId), tag);
			response = Response.status(200).build();
		} catch (Exception e) {
			logger.error(e);
			response = Response.serverError().entity(new ErrorBean(500, "Unexpected Error")).build();
		}

		return response;

	}

	@DELETE
	@Consumes({ MediaType.APPLICATION_JSON })
	@Path("/propertyId/{propertyId}/tag/{tag}")
	@Secured
	public Response removeTag(@PathParam("propertyId") String propertyId, @PathParam("tag") String tag) {
		Response response = null;

		try {
			propService.removeTag(new ObjectId(propertyId), tag);
			response = Response.status(200).build();
		} catch (Exception e) {
			logger.error(e);
			response = Response.serverError().entity(new ErrorBean(500, "Unexpected Error")).build();
		}

		return response;

	}

	@GET
	@Consumes({ MediaType.APPLICATION_JSON })
	@Path("/propertyId/{propertyId}/")
	@Secured
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
