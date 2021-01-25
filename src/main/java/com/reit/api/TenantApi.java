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
import com.reit.beans.ErrorBean;
import com.reit.beans.saas.tenant.SaasTenantBean;
import com.reit.services.SaasTenantService;
import com.reit.util.Secured;

@Path("/reit/tenant")
public class TenantApi {
	private LogManager logger = LogManager.getLogger(this.getClass());
	private Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();

	@POST
	@Produces({ MediaType.APPLICATION_JSON })
	@Secured
	public Response createTenant(@NotNull(message = "API payload can't be null") SaasTenantBean saasTenant) {
		Response response = null;

		try {
			SaasTenantService tenantService = new SaasTenantService(saasTenant);
			tenantService.createTenant();

			response = Response.status(201).entity("{}").build();
		} catch (NotAuthorizedException e) {
			logger.error(e);
			response = Response.status(401).entity(new ErrorBean(401, "Unexpected Error")).build();
		} catch (EntityNotFoundException e) {
			logger.error(e);
			response = Response.status(400).entity(new ErrorBean(400, "Unexpected Error")).build();
		} catch (Exception e) {
			logger.error(e);
			response = Response.status(500).entity(new ErrorBean(500, "Unexpected Error")).build();

		}

		return response;

	}
}
