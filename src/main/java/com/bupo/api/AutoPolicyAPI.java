package com.bupo.api;

import javax.persistence.EntityNotFoundException;
import javax.validation.constraints.NotNull;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.bupo.beans.AutoPolicyRequest;
import com.bupo.dao.model.UserAuto;
import com.bupo.services.AutoPolicyService;
import com.bupo.util.JsonUtil;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

@Path("/auto")
public class AutoPolicyAPI {

	@POST
	@Consumes({ MediaType.APPLICATION_JSON })
	public Response saveAutoDetails(
			@NotNull(message = "API payload can't be null") AutoPolicyRequest autoPolicyRequest) {
		Response response = null;

		try {
			AutoPolicyService autoPolicyService = new AutoPolicyService();
			String uniqueCode = autoPolicyService.createAutoPolcyRequest(autoPolicyRequest);
			JsonObject responseObj = new JsonObject();
			responseObj.addProperty("uniqueCode", uniqueCode);

			response = Response.status(201).entity(responseObj.toString()).build();
		} catch (Exception e) {
			response = Response.serverError().build();
		}

		return response;

	}

	@GET
	@Path("/search")
	public Response getAutoDetails(@QueryParam("code") String code, @QueryParam("lastName") String lastName,
			@QueryParam("zip") String zip) {
		Response response = null;

		try {
			AutoPolicyService autoPolicyService = new AutoPolicyService();
			UserAuto userAuto = autoPolicyService.getAutoPolicyDetails(code, lastName, zip);

			// crapy work around as string contains a json. Couldn't find a better solution.
			JsonElement jsonElement = new Gson().toJsonTree(userAuto);
			if (JsonUtil.isJson(userAuto.getPolicyRequestDetails())) {
				JsonObject jsonObject = JsonParser.parseString(userAuto.getPolicyRequestDetails()).getAsJsonObject();
				jsonElement.getAsJsonObject().add("policyRequestDetails", jsonObject);
			}

			response = Response.status(200).entity(jsonElement.toString()).build();
		} catch (EntityNotFoundException e) {
			JsonObject responseObj = new JsonObject();
			responseObj.addProperty("error", "No Results Found");
			response = Response.status(400).entity(responseObj.toString()).build();
		} catch (Exception e) {
			e.printStackTrace();
			response = Response.serverError().build();
		}

		return response;

	}

}
