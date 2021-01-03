package com.reit.api;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityNotFoundException;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.bupo.beans.SearchResultBean;
import com.bupo.util.LogManager;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.reit.beans.PropertyResultsBean;
import com.reit.beans.SearchFilter;
import com.reit.enums.FilterOperator;
import com.reit.services.PropertyService;

@Path("/reit/property")
public class PropertyAPI {
	private LogManager logger = LogManager.getLogger(this.getClass());
	private PropertyService propertyService = new PropertyService();
	private Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();

	@POST
	@Produces({ MediaType.APPLICATION_JSON })
	public Response getProperties() {
		Response response = null;
		SearchResultBean<PropertyResultsBean> resultBean = new SearchResultBean<PropertyResultsBean>();

		try {
			List<SearchFilter> filters = new ArrayList<SearchFilter>();
			SearchFilter<Double> filter = new SearchFilter("cap", 3, FilterOperator.GREATER_THAN);
			filters.add(filter);
			List<PropertyResultsBean> properties = propertyService.findMatchingProperties(filters);

			resultBean.setCount(1);
			resultBean.setResults(properties);

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

}
