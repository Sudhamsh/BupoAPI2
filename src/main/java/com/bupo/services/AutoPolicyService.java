package com.bupo.services;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.beanutils.BeanUtils;

import com.bupo.beans.AutoPolicyRequest;
import com.bupo.dao.BaseDao;
import com.bupo.dao.model.UserAuto;
import com.bupo.util.RandomString;
import com.google.common.base.Preconditions;
import com.google.gson.Gson;

public class AutoPolicyService {

	/**
	 * Persists auto policy Request. No user auth required
	 * 
	 * @param autoPolicyRequest
	 */
	public String createAutoPolcyRequest(AutoPolicyRequest autoPolicyRequest) {
		Preconditions.checkNotNull(autoPolicyRequest, "autoPolicy is null");

		BaseDao baseDao = new BaseDao();
		UserAuto userAuto = new UserAuto();
		String uniqueCode = null;

		try {
			BeanUtils.copyProperties(userAuto, autoPolicyRequest);
			uniqueCode = RandomString.getAlphaNumericString(20, 5);
			// TODO check if the code exists in DB. Odds are low but we still need too :)
			userAuto.setCode(uniqueCode);
			userAuto.setPolicyRequestDetails(new Gson().toJson(autoPolicyRequest));

			baseDao.create(userAuto);
		} catch (IllegalAccessException | InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return uniqueCode;
	}

	public UserAuto getAutoPolicyDetails(String code, String lastName, String zip) {
		UserAuto userAuto = null;

		BaseDao baseDao = new BaseDao();
		Map<String, String> paramsMap = new HashMap<String, String>();
		paramsMap.put("code", code);
		paramsMap.put("lastName", lastName);
		paramsMap.put("zip", zip);

		userAuto = baseDao.findByNamedQuery("findAutoByCodeByLnameByZip", paramsMap, UserAuto.class).get(0);

		// Set id to 0
		userAuto.setId(0);

		return userAuto;

	}
}
