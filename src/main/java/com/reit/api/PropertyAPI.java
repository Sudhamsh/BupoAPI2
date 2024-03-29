package com.reit.api;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityNotFoundException;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.SecurityContext;

import org.bson.types.ObjectId;

import com.bupo.beans.SearchResultBean;
import com.bupo.exceptions.ObjectExists;
import com.bupo.util.LogManager;
import com.google.gson.Gson;
import com.reit.beans.ErrorBean;
import com.reit.beans.NotesBean;
import com.reit.beans.PropertyResultsBean;
import com.reit.beans.SearchFilter;
import com.reit.beans.StatusBean;
import com.reit.enums.FilterOperator;
import com.reit.services.PropertyService;
import com.reit.services.TokenService;
import com.reit.util.GsonUtils;
import com.reit.util.Secured;

@Path("/reit/property")
public class PropertyAPI {
	private LogManager logger = LogManager.getLogger(this.getClass());
	private PropertyService propertyService = new PropertyService();
	private Gson gson = GsonUtils.getGson();
	// GsonBuilder.serializeSpecialFloatingPointValues()

	@POST
	@Produces({ MediaType.APPLICATION_JSON })
	public Response getProperties() {
		Response response = null;
		SearchResultBean<PropertyResultsBean> resultBean = new SearchResultBean<PropertyResultsBean>();

		try {
			List<SearchFilter> filters = new ArrayList<SearchFilter>();
			SearchFilter<String> filter = new SearchFilter("status", "New", FilterOperator.EQUALS);
			filters.add(filter);

			SearchFilter<Double> remainingFilter = new SearchFilter("remainingTerm", 5, FilterOperator.GREATER_THAN);
			filters.add(remainingFilter);

			List<PropertyResultsBean> properties = propertyService.findMatchingProperties(filters);
			System.out.println("Si" + properties.size());
			System.out.println("data" + gson.toJson(properties));

			resultBean.setCount(1);
			resultBean.setResults(properties);

			System.out.println("data 1" + gson.toJson(resultBean));

			response = Response.status(200).entity(gson.toJson(resultBean)).build();
		} catch (EntityNotFoundException e) {
			logger.error(e);
			resultBean.setCount(0);
			resultBean.setMessage("No Results Found");
			response = Response.status(400).entity(resultBean).build();
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e);
			resultBean.setCount(0);
			resultBean.setMessage("Server Error");
			response = Response.status(500).entity(gson.toJson(resultBean)).build();

		}

		return response;
	}

	@PUT
	@Path("/propertyId/{propertyId}/notes")
	@Secured
	public Response addNotes(@PathParam("propertyId") String propertyId, NotesBean note,
			@Context SecurityContext securityContext) {
		Response response = null;

		try {
			String loggedInUser = securityContext.getUserPrincipal().getName();
			note.setUserEmail(loggedInUser);
			propertyService.addNotes(new ObjectId(propertyId), note);

			response = Response.status(200).build();
		} catch (ObjectExists e) {
			logger.error(e);
			response = Response.status(Status.CONFLICT).entity(Status.NO_CONTENT).build();
		} catch (Exception e) {
			logger.error(e);
			response = Response.serverError().entity(new ErrorBean(500, "Unexpected Error")).build();
		}

		return response;

	}

	@PUT
	@Path("/propertyId/{propertyId}/status")
	@Secured
	public Response updateStatus(@PathParam("propertyId") String propertyId, StatusBean status,
			@Context SecurityContext securityContext) {
		Response response = null;

		try {
			String loggedInUser = securityContext.getUserPrincipal().getName();
			status.setUserEmail(loggedInUser);
			propertyService.updateStatus(new ObjectId(propertyId), status);

			response = Response.status(200).build();
		} catch (ObjectExists e) {
			logger.error(e);
			response = Response.status(Status.CONFLICT).entity(Status.NO_CONTENT).build();
		} catch (Exception e) {
			logger.error(e);
			response = Response.serverError().entity(new ErrorBean(500, "Unexpected Error")).build();
		}

		return response;

	}

	@PUT
	@Path("/propertyId/{propertyId}/caps")
	@Secured
	public Response updateCaps(@PathParam("propertyId") String propertyId, List<Long> capsList,
			@Context SecurityContext securityContext) {
		Response response = null;

		try {

			propertyService.updateCaps(new ObjectId(propertyId), capsList);

			response = Response.status(200).build();
		} catch (ObjectExists e) {
			logger.error(e);
			response = Response.status(Status.CONFLICT).entity(Status.NO_CONTENT).build();
		} catch (Exception e) {
			logger.error(e);
			response = Response.serverError().entity(new ErrorBean(500, "Unexpected Error")).build();
		}

		return response;

	}

	@GET
	@Secured
	@Consumes({ MediaType.APPLICATION_JSON })
	@Path("/propertyId/{propertyId}/notes")
	public Response getNotes(@PathParam("propertyId") String propertyId) {
		Response response = null;

		try {
			TokenService tokenService = new TokenService();

			List<NotesBean> notes = propertyService.getNotes(new ObjectId(propertyId), tokenService.getTenantName());

			response = Response.status(200).entity(gson.toJson(notes)).build();
		} catch (Exception e) {
			logger.error(e);
			response = Response.serverError().entity(new ErrorBean(500, "Unexpected Error")).build();
		}

		return response;

	}

}
