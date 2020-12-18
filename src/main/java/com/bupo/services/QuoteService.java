package com.bupo.services;

import static com.mongodb.client.model.Filters.eq;

import java.util.HashMap;
import java.util.Map;

import javax.persistence.EntityNotFoundException;

import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;

import com.bupo.beans.AgentQuote;
import com.bupo.beans.AutoQuote;
import com.bupo.beans.QuoteSummary;
import com.bupo.beans.QuotesBean;
import com.bupo.beans.QuotesRequestBean;
import com.bupo.dao.MongoDao;
import com.bupo.enums.MongoCollEnum;
import com.bupo.util.LogManager;
import com.google.common.base.Preconditions;
import com.google.gson.Gson;

public class QuoteService {
	Gson gson = new Gson();
	MongoDao mongoDao = new MongoDao();
	private LogManager logger = LogManager.getLogger(this.getClass());

	public void addQuote(QuotesRequestBean quotesRequest) {
		Preconditions.checkNotNull(quotesRequest, "quotes is null");
		Preconditions.checkNotNull(quotesRequest.getCode(), "code is null");
		Preconditions.checkNotNull(quotesRequest.getProvider(), "Provider is null");

		try {

			Bson filter = eq("code", quotesRequest.getCode());

			// Persist quote details
			Map<String, QuoteSummary> quotes = new HashMap<String, QuoteSummary>();
			for (AutoQuote autoQuote : quotesRequest.getQuotes()) {
				ObjectId objectId = mongoDao.insert(MongoCollEnum.QuoteDetail.toString(), gson.toJson(autoQuote));

				QuoteSummary quoteSummary = new QuoteSummary();
				quoteSummary.setDetailId(objectId.toHexString());
				quoteSummary.setMonthlyPremiuim(getQuoteTotal(autoQuote));
				// TODO capture monthly and single payment details seperate from the agent
				quoteSummary.setSinglePaymentPremiuim(getQuoteTotal(autoQuote) / 6);

				quotes.put(autoQuote.getQuoteType().toString(), quoteSummary);

			}
			AgentQuote agentQuote = new AgentQuote(quotes);

			Document quoteDoc = mongoDao.findOne(MongoCollEnum.Quote.toString(), filter);

			// Get DB value if it exists
			Map<String, AgentQuote> providerQuotes = new HashMap<String, AgentQuote>();

			if (quoteDoc == null) {
				// Insert
				providerQuotes.put(quotesRequest.getProvider(), agentQuote);
				QuotesBean quotesBean = new QuotesBean(quotesRequest.getCode(), providerQuotes);

				mongoDao.insert(MongoCollEnum.Quote.toString(), gson.toJson(quotesBean));
			} else {
				// update
				QuotesBean dbQuotes = gson.fromJson(quoteDoc.toJson(), QuotesBean.class);
				providerQuotes = dbQuotes.getProviderQuotes();
				providerQuotes.put(quotesRequest.getProvider(), agentQuote);
				mongoDao.findAndReplace(MongoCollEnum.Quote.toString(), filter, gson.toJson(dbQuotes));
			}

		} catch (EntityNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new EntityNotFoundException("Not Found");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private int getQuoteTotal(AutoQuote autoQuote) {
		int total = 0;

		total += autoQuote.getBilPremium();
		total += autoQuote.getPdlPremium();
		total += autoQuote.getMpPremium();
		total += autoQuote.getUniPremium();

		return total;
	}

	public Document getQuote(String code) {
		Preconditions.checkNotNull(code, "quotes is null");

		Bson filter = eq("code", code);
		Document quotesDoc = mongoDao.findOne(MongoCollEnum.Quote.toString(), filter);
		logger.debug(String.format("Quotes for code: %s", code));

		return quotesDoc;

	}

}
