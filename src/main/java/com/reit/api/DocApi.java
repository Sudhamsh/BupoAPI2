package com.reit.api;

import java.util.Map;

import javax.persistence.EntityNotFoundException;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.bupo.util.LogManager;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.reit.beans.ErrorBean;
import com.reit.services.GenerateDocService;

@Path("/reit/doc")
public class DocApi {
	private LogManager logger = LogManager.getLogger(this.getClass());
	private GenerateDocService docService = new GenerateDocService();
	private Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();

	@POST
	@Produces({ MediaType.APPLICATION_JSON })
	public Response createDoc(Map<String, String> variablesMap) {
		Response response = null;

		try {
			docService.generateDoc("dev", "familyDollar_" + System.currentTimeMillis(), "LOI", variablesMap);

			response = Response.status(201).entity("{}").build();
		} catch (EntityNotFoundException e) {
			logger.error(e);
			response = Response.serverError().entity(new ErrorBean(500, "Unexpected Error")).build();
		} catch (Exception e) {
			logger.error(e);
			response = Response.serverError().entity(new ErrorBean(500, "Unexpected Error")).build();
		}

		return response;

	}
}
