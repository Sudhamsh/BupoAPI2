package com.reit.api;

import javax.persistence.EntityNotFoundException;
import javax.validation.constraints.NotNull;
import javax.ws.rs.NotAuthorizedException;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.bupo.util.LogManager;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.reit.beans.saas.tenant.SaasTenantBean;
import com.reit.services.SaasTenantService;

@Path("/reit/tenant")
public class TenantApi {
	private LogManager logger = LogManager.getLogger(this.getClass());
	private Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();

	@POST
	@Produces({ MediaType.APPLICATION_JSON })
	public Response createTenant(@NotNull(message = "API payload can't be null") SaasTenantBean saasTenant) {
		Response response = null;

		try {
			SaasTenantService tenantService = new SaasTenantService(saasTenant);
			tenantService.createTenant();

			response = Response.status(201).entity(null).build();
		} catch (NotAuthorizedException e) {
			logger.error(e);
			response = Response.status(401).entity(null).build();
		} catch (EntityNotFoundException e) {
			logger.error(e);
			response = Response.status(400).entity(null).build();
		} catch (Exception e) {
			logger.error(e);
			response = Response.status(500).entity(gson.toJson(null)).build();

		}

		return response;

	}
}
