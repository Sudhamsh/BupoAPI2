package com.bupo.api;

import javax.persistence.EntityNotFoundException;
import javax.validation.constraints.NotNull;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;

import org.bson.Document;

import com.bupo.beans.QuotesRequestBean;
import com.bupo.beans.SearchResultBean;
import com.bupo.services.QuoteService;
import com.bupo.util.LogManager;
import com.google.gson.Gson;

@Path("/quote")
public class QuoteAPI {
	QuoteService quoteService = new QuoteService();
	Gson gson = new Gson();
	private LogManager logger = LogManager.getLogger(this.getClass());

	@PUT
	@Path("/code/{code}")
	public Response updateQuote(@PathParam("code") String code,
			@NotNull(message = "Payload can't be null") QuotesRequestBean quotesRequest) {
		Response response = null;
		try {
			quotesRequest.setCode(code);
			quoteService.addQuote(quotesRequest);
		} catch (EntityNotFoundException e) {
			response = Response.status(204).entity("Not found").build();
		} catch (Exception e) {
			logger.error(e);
			response = Response.serverError().build();
		}

		return response;

	}

	@GET
	@Path("/code/{code}")
	public Response getQuote(@PathParam("code") String code) {
		Response response = null;
		SearchResultBean<Document> resultBean = new SearchResultBean<Document>();
		resultBean.setSearchParam("code", code);

		try {
			Document quotes = quoteService.getQuote(code);
			resultBean.setCount(1);
			resultBean.getResults().add(quotes);
			response = Response.status(200).entity(gson.toJson(resultBean)).build();
		} catch (EntityNotFoundException e) {
			response = Response.status(204).entity("Not found").build();
		} catch (Exception e) {
			response = Response.serverError().build();
		}

		return response;

	}

}
