package com.reit.api;

import java.util.HashMap;
import java.util.Map;

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
import com.reit.services.GenerateDocService;

@Path("/reit/doc")
public class DocApi {
	private LogManager logger = LogManager.getLogger(this.getClass());
	private GenerateDocService docService = new GenerateDocService();
	private Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();

	@POST
	@Produces({ MediaType.APPLICATION_JSON })
	public Response createDoc() {
		Response response = null;
		SearchResultBean<PropertyResultsBean> resultBean = new SearchResultBean<PropertyResultsBean>();

		try {

			Map<String, String> variablesMap = new HashMap<>();
			variablesMap.put("purchasePrice", "1,000,000");
			variablesMap.put("loi_amount", "100,000");

			docService.generateDoc("dev", "familyDollar", "LOI", variablesMap);

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
