package com.bupo.services;

import java.util.List;

import javax.persistence.EntityNotFoundException;

import com.bupo.dao.BaseDao;
import com.bupo.dao.model.UserAuto;
import com.bupo.util.JsonUtil;
import com.google.common.base.Preconditions;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonParser;

public class QuoteService {
	Gson gson = new Gson();

	public void addQuote(String code, String newQuoteJson) {
		Preconditions.checkNotNull(code, "code is null");
		Preconditions.checkNotNull(newQuoteJson, "quote is null");

		BaseDao baseDao = new BaseDao();

		try {
			List<UserAuto> resultList = baseDao.findByField(UserAuto.class, "code", code, 1);
			UserAuto userAuto = resultList.get(0);

			String existingQuote = userAuto.getQuotes();
			JsonArray quotes = null;
			if (JsonUtil.isJson(existingQuote)) {
				quotes = JsonParser.parseString(existingQuote).getAsJsonArray();
				quotes.add(JsonParser.parseString(newQuoteJson));
			} else {
				quotes = new JsonArray();
				quotes.add(JsonParser.parseString(newQuoteJson));
			}

			userAuto.setQuotes(gson.toJson(quotes));
			baseDao.update(userAuto);
		} catch (EntityNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new EntityNotFoundException("Not Found");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
