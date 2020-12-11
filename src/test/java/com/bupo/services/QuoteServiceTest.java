package com.bupo.services;

import java.util.ArrayList;
import java.util.List;

import org.bson.Document;
import org.junit.Assert;
import org.junit.Test;

import com.bupo.beans.AutoPolicyRequest;
import com.bupo.beans.AutoQuote;
import com.bupo.beans.QuotesRequestBean;
import com.bupo.enums.QuoteType;

public class QuoteServiceTest {

	QuoteService quoteService = new QuoteService();

	@Test
	public void addQuote_hp() {
		try {
			String code = addQuote();

			Document quotesDoc = quoteService.getQuote(code);
			Assert.assertTrue("Get Qutoes failed, returned null.", quotesDoc != null);
		} catch (Exception e) {
			e.printStackTrace();
			Assert.fail("Add quote failed : " + e.getMessage());
		}
	}

	@Test
	public void getQuote_hp() {
		try {
			String code = addQuote();
			Document quotesDoc = quoteService.getQuote(code);
			Assert.assertTrue("Get Qutoes failed, returned null.", quotesDoc != null);

			System.out.println(quotesDoc.toString());

		} catch (Exception e) {
			e.printStackTrace();
			Assert.fail("Add quote failed : " + e.getMessage());
		}
	}

	public String addQuote() {
		// Create request
		AutoPolicyRequest autoPolicyRequest = new AutoPolicyRequest();
		autoPolicyRequest.setLastName("LastName");
		autoPolicyRequest.getAddress().setZip("40000");

		AutoPolicyService autoPolicyService = new AutoPolicyService();
		String code = autoPolicyService.createAutoPolcyRequest(autoPolicyRequest);
		System.out.println("code:" + code);

		QuotesRequestBean quotesRequestBean = new QuotesRequestBean();
		quotesRequestBean.setCode(code);
		quotesRequestBean.setProvider("Gieco");
		List<AutoQuote> autoQuotes = new ArrayList<AutoQuote>();

		// three quotes quote
		AutoQuote autoQuote = new AutoQuote(10, 20, 30, 40, "100K/300K", "10K/30K", "10K/30K", "100K/300K",
				QuoteType.Recommended);
		autoQuotes.add(autoQuote);
		autoQuote = new AutoQuote(5, 10, 20, 30, "100K/100K", "10K/30K", "10K/30K", "100K/100K", QuoteType.Minimum);
		autoQuotes.add(autoQuote);

		autoQuote = new AutoQuote(40, 50, 60, 70, "500K/500K", "10K/30K", "10K/30K", "500K/500K", QuoteType.Expensive);
		autoQuotes.add(autoQuote);

		quotesRequestBean.setQuotes(autoQuotes);
		quoteService.addQuote(quotesRequestBean);

		// Update quote with second provider
		quotesRequestBean = new QuotesRequestBean();
		quotesRequestBean.setCode(code);
		quotesRequestBean.setProvider("Progressive");
		autoQuotes = new ArrayList<AutoQuote>();

		// three quotes quote
		autoQuote = new AutoQuote(15, 25, 35, 45, "100K/300K", "10K/30K", "10K/30K", "100K/300K",
				QuoteType.Recommended);
		autoQuotes.add(autoQuote);
		autoQuote = new AutoQuote(5, 15, 25, 35, "100K/100K", "10K/30K", "10K/30K", "100K/100K", QuoteType.Minimum);
		autoQuotes.add(autoQuote);

		autoQuote = new AutoQuote(45, 55, 65, 75, "500K/500K", "10K/30K", "10K/30K", "500K/500K", QuoteType.Expensive);
		autoQuotes.add(autoQuote);

		quotesRequestBean.setQuotes(autoQuotes);
		quoteService.addQuote(quotesRequestBean);

		return code;
	}

	@Test
	public void addQuote_cc() {

	}

}
