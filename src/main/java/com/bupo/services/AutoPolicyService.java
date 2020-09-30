package com.bupo.services;

import static com.mongodb.client.model.Filters.and;
import static com.mongodb.client.model.Filters.eq;

import java.util.ArrayList;
import java.util.List;

import org.bson.Document;
import org.bson.conversions.Bson;

import com.bupo.beans.AutoPolicyRequest;
import com.bupo.dao.MongoDao;
import com.bupo.dao.model.UserAuto;
import com.bupo.enums.MongoCollEnum;
import com.bupo.util.LogManager;
import com.bupo.util.RandomString;
import com.google.common.base.Preconditions;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mongodb.client.FindIterable;

public class AutoPolicyService {
	private LogManager logger = LogManager.getLogger(this.getClass());
	private Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();

	/**
	 * Persists auto policy Request. No user auth required
	 * 
	 * @param autoPolicyRequest
	 */
	public String createAutoPolcyRequest(AutoPolicyRequest autoPolicyRequest) {
		Preconditions.checkNotNull(autoPolicyRequest, "autoPolicy is null");

		MongoDao mongoDao = new MongoDao();
		String uniqueCode = null;

		try {

			uniqueCode = RandomString.getAlphaNumericString(20, 5);
			// TODO check if the code exists in DB. Odds are low but we still need too :)
			autoPolicyRequest.setCode(uniqueCode);

			mongoDao.insert("autoHome", gson.toJson(autoPolicyRequest));

		} catch (Exception e) {
			logger.error(e);
			throw new RuntimeException(e);
		}
		return uniqueCode;
	}

	/**
	 * Updates existing auto policy Request. No user auth required
	 * 
	 * @param autoPolicyRequest
	 */
	public void updateAutoPolicyRequest(AutoPolicyRequest autoPolicyRequest) {
		Preconditions.checkNotNull(autoPolicyRequest, "autoPolicy is null");
		Preconditions.checkNotNull(autoPolicyRequest.getCode(), "requestCode is null");
		MongoDao mongoDao = new MongoDao();

		try {
			mongoDao.findAndReplace("autoHome", eq("code", autoPolicyRequest.getCode()),
					gson.toJson(autoPolicyRequest));
		} catch (Exception e) {
			logger.error(e);
			throw new RuntimeException(e);
		}
	}

	public Document getAutoPolicyDetails(String code, String lastName, String zip) {

		MongoDao mongoDao = new MongoDao();
		Bson filter = and(eq("code", code), eq("lastName", lastName), eq("address.zip", zip));
		Document policyRequest = mongoDao.findOne(MongoCollEnum.AutoHome.toString(), filter);

		return policyRequest;

	}

	public List<UserAuto> getOpenAutoPolicy() {

		MongoDao mongoDao = new MongoDao();
		List<UserAuto> userAutos = new ArrayList<UserAuto>();

		FindIterable<Document> policyRequests = mongoDao.find(MongoCollEnum.AutoHome.toString(), null, 10);

		for (Document doc : policyRequests) {
			UserAuto userAuto = new UserAuto();
			userAuto.setCode(doc.getString("code"));
			Document address = doc.get("address", Document.class);
			userAuto.setZip(address.getString("zip"));
			userAuto.setStatus(doc.getString("status"));

			userAutos.add(userAuto);
		}

		return userAutos;

	}

	public Document getAutoPolicyDetails(String code, String userRole, boolean hidePersonalInfo) {
		MongoDao mongoDao = new MongoDao();
		Bson filter = eq("code", code);
		Document policyRequest = mongoDao.findOne(MongoCollEnum.AutoHome.toString(), filter);

		// Hide Personal Info
		if (hidePersonalInfo) {
			// userAuto.anonymizePersonalData();
		}

		return policyRequest;

	}
}
