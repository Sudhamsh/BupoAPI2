package com.bupo.services;

import com.bupo.beans.AutoPolicy;
import com.google.gson.JsonObject;

public class AutoExtractorImpl implements FileExtractor {

	@Override
	public JsonObject getStrcturedData(String inputStr) {

		throw new RuntimeException("Not implemented yet :)");

		// return null;
	}

	public void persistAutoPolicyDetails(AutoPolicy autoPolicy) {

//		Preconditions.checkNotNull(autoPolicy, "autoPolicy is null");
//
//		// Save details to DB
//		BaseDao baseDao = new BaseDao();
//
//		UserAuto userAuto = baseDao.findById(UserAuto.class, userEmailId);
//
//		// Adds or appends URLs in DB
//		if (userAuto == null) {
//			userAuto = new UserAuto();
//			userAuto.setEmailId(userEmailId);
//			Gson gson = new Gson();
//			JsonObject policyObject = new JsonObject();
//			System.out.println("details : " + gson.toJson(autoPolicy));
//			policyObject.add(fileUrl, gson.toJsonTree(autoPolicy));
//			userAuto.setPolicyDetails(policyObject.toString());
//			baseDao.create(userAuto);
//
//		} else {
//			Gson gson = new Gson();
//			String existingPolicyDetails = userAuto.getPolicyDetails();
//			JsonObject policyObject = JsonUtil.isJson(existingPolicyDetails)
//					? gson.fromJson(existingPolicyDetails, JsonObject.class)
//					: new JsonObject();
//			System.out.println("details : " + gson.toJson(autoPolicy));
//			policyObject.add(fileUrl, gson.toJsonTree(autoPolicy));
//			userAuto.setPolicyDetails(policyObject.toString());
//			baseDao.update(UserAuto.class, userAuto);
//		}

	}

}
