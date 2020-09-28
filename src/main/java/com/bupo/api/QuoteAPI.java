package com.bupo.api;

import javax.persistence.EntityNotFoundException;
import javax.validation.constraints.NotNull;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;

import com.bupo.services.QuoteService;

@Path("/quote")
public class QuoteAPI {
	QuoteService quoteService = new QuoteService();

	@PUT
	@Path("/code/{code}")
	public Response updateQuote(@PathParam("code") String code,
			@NotNull(message = "Payload can't be null") String payLoad) {
		Response response = null;
		try {
			quoteService.addQuote(code, payLoad);
		} catch (EntityNotFoundException e) {
			response = Response.status(204).entity("Not found").build();
		} catch (Exception e) {
			response = Response.serverError().build();
		}

		return response;

	}
}
