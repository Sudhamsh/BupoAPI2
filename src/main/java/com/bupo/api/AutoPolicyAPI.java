package com.bupo.api;

import java.util.List;

import javax.persistence.EntityNotFoundException;
import javax.validation.constraints.NotNull;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.bson.Document;

import com.bupo.beans.AutoPolicyRequest;
import com.bupo.beans.SearchResultBean;
import com.bupo.dao.model.UserAuto;
import com.bupo.services.AutoPolicyService;
import com.bupo.util.JsonUtil;
import com.bupo.util.LogManager;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

@Path("/auto")
public class AutoPolicyAPI {
	AutoPolicyService autoPolicyService = new AutoPolicyService();
	Gson gson = new Gson();
	private LogManager logger = LogManager.getLogger(this.getClass());

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

	@PUT
	@Consumes({ MediaType.APPLICATION_JSON })
	public Response updateAutoDetails(
			@NotNull(message = "API payload can't be null") AutoPolicyRequest autoPolicyRequest) {
		Response response = null;

		try {
			AutoPolicyService autoPolicyService = new AutoPolicyService();
			autoPolicyService.updateAutoPolicyRequest(autoPolicyRequest);
			JsonObject responseObj = new JsonObject();
			responseObj.addProperty("uniqueCode", autoPolicyRequest.getCode());

			response = Response.status(204).entity(responseObj.toString()).build();
		} catch (Exception e) {
			response = Response.serverError().build();
		}

		return response;

	}

	@GET
	@Path("/code/{code}")
	@Produces({ MediaType.APPLICATION_JSON })
	public Response getAutoDetailsByCode(@PathParam("code") String code) {
		Response response = null;
		SearchResultBean<Document> resultBean = new SearchResultBean<Document>();
		resultBean.setSearchParam("code", code);

		try {
			Document userAuto = autoPolicyService.getAutoPolicyDetails(code, null, true);
			resultBean.setCount(1);
			resultBean.getResults().add(userAuto);

			response = Response.status(200).entity(gson.toJson(resultBean)).build();
		} catch (EntityNotFoundException e) {
			logger.error(e);
			resultBean.setCount(0);
			resultBean.setMessage("No Results Found");
			response = Response.status(400).entity(resultBean).build();
		} catch (Exception e) {
			logger.error(e);
			resultBean.setCount(0);
			resultBean.setMessage("Server Error");
			response = Response.status(500).entity(gson.toJson(resultBean)).build();

		}

		return response;
	}

	private JsonElement getJsonFromResultBean(SearchResultBean<UserAuto> resultBean, String policyDetails) {
		JsonElement jsonElement = gson.toJsonTree(resultBean);

		if (JsonUtil.isJson(policyDetails)) {
			JsonObject jsonObject = JsonParser.parseString(policyDetails).getAsJsonObject();
			jsonElement.getAsJsonObject().getAsJsonArray("results").set(0, jsonObject);
		}

		return jsonElement;
	}

	@GET
	@Path("/search")
	public Response getAutoDetails(@QueryParam("code") String code, @QueryParam("lastName") String lastName,
			@QueryParam("zip") String zip) {
		Response response = null;
		SearchResultBean<Document> resultBean = new SearchResultBean<Document>();

		// set params
		resultBean.setSearchParam("code", code);
		resultBean.setSearchParam("lastName", lastName);
		resultBean.setSearchParam("zip", zip);

		try {
			Document userAuto = autoPolicyService.getAutoPolicyDetails(code, lastName, zip);
			resultBean.setCount(1);
			resultBean.getResults().add(userAuto);

			response = Response.status(200).entity(resultBean).build();
		} catch (EntityNotFoundException e) {

			resultBean.setCount(0);
			resultBean.setMessage("No Results Found");
			response = Response.status(400).entity(gson.toJson(resultBean)).build();
		} catch (Exception e) {
			e.printStackTrace();
			resultBean.setCount(0);
			resultBean.setMessage("Server Error");
			response = Response.status(500).entity(gson.toJson(resultBean)).build();

		}

		return response;

	}

	@GET
	@Path("/search/status/open")
	public Response getOpenRequests() {
		Response response = null;
		SearchResultBean<UserAuto> resultBean = new SearchResultBean<UserAuto>();

		try {
			List<UserAuto> userAutos = autoPolicyService.getOpenAutoPolicy();
			resultBean.setCount(userAutos.size());
			resultBean.setResults(userAutos);

			response = Response.status(200).entity(gson.toJson(resultBean)).build();
		} catch (EntityNotFoundException e) {

			resultBean.setCount(0);
			resultBean.setMessage("No Results Found");
			response = Response.status(400).entity(gson.toJson(resultBean)).build();
		} catch (Exception e) {
			e.printStackTrace();
			resultBean.setCount(0);
			resultBean.setMessage("Server Error");
			response = Response.status(500).entity(gson.toJson(resultBean)).build();

		}

		return response;

	}

}
