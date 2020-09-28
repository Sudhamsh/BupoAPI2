package com.bupo.services;

import org.junit.Assert;
import org.junit.Test;

import com.bupo.beans.AutoPolicyRequest;
import com.google.gson.JsonObject;

public class QuoteServiceTest {

	QuoteService quoteService = new QuoteService();

	@Test
	public void addQuote_hp() {

		// Create request
		AutoPolicyRequest autoPolicyRequest = new AutoPolicyRequest();
		autoPolicyRequest.setLastName("LastName");
		autoPolicyRequest.getAddress().setZip("40000");

		AutoPolicyService autoPolicyService = new AutoPolicyService();
		String code = autoPolicyService.createAutoPolcyRequest(autoPolicyRequest);
		JsonObject quoteObj = new JsonObject();
		quoteObj.addProperty("company", "Gieco");
		quoteObj.add("lowest", new JsonObject());
		quoteObj.add("high", new JsonObject());
		quoteService.addQuote(code, quoteObj.toString());

		// add one more
		quoteObj = new JsonObject();
		quoteObj.addProperty("company", "Progressive");
		quoteObj.add("lowest", new JsonObject());
		quoteObj.add("high", new JsonObject());
		quoteService.addQuote(code, quoteObj.toString());

	}

	@Test
	public void addQuote_cc() {

		try {
			JsonObject quoteObj = new JsonObject();
			quoteObj.addProperty("company", "Progressive");
			quoteObj.add("lowest", new JsonObject());
			quoteObj.add("high", new JsonObject());
			quoteService.addQuote("xyz1213a12312asdf24", quoteObj.toString());
			Assert.fail("Accepted random quote");
		} catch (Exception e) {
			// TODO: handle exception
		}

	}

}
