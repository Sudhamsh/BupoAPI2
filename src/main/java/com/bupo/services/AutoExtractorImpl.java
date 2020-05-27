package com.bupo.services;

import com.bupo.beans.AutoPolicy;
import com.bupo.dao.BaseDao;
import com.bupo.dao.model.UserAuto;
import com.bupo.util.JsonUtil;
import com.google.common.base.Preconditions;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

public class AutoExtractorImpl implements FileExtractor {

	@Override
	public JsonObject getStrcturedData(String inputStr) {

		throw new RuntimeException("Not implemented yet :)");

		// return null;
	}

	public void persistAutoPolicyDetails(String userEmailId, String fileUrl, AutoPolicy autoPolicy) {

		Preconditions.checkNotNull(userEmailId, "User Email Id is null");
		Preconditions.checkNotNull(fileUrl, "FileUrl is null");
		Preconditions.checkNotNull(userEmailId, "autoPolicy is null");

		// Save details to DB
		BaseDao baseDao = new BaseDao();

		UserAuto userAuto = baseDao.findById(UserAuto.class, userEmailId);

		// Adds or appends URLs in DB
		if (userAuto == null) {
			throw new RuntimeException("User Not found");

		} else {
			Gson gson = new Gson();
			String existingPolicyDetails = userAuto.getPolicyDetails();
			JsonObject policyObject = JsonUtil.isJson(existingPolicyDetails)
					? gson.fromJson(existingPolicyDetails, JsonObject.class)
					: new JsonObject();
			System.out.println("details : " + gson.toJson(autoPolicy));
			policyObject.add(fileUrl, gson.toJsonTree(autoPolicy));
			userAuto.setPolicyDetails(policyObject.toString());
			baseDao.update(UserAuto.class, userAuto);
		}

	}

}
