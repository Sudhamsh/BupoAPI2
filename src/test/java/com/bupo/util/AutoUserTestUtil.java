package com.bupo.util;

import com.bupo.dao.model.UserAuto;

public class AutoUserTestUtil {

	public static UserAuto createUser() {

		UserAuto userAuto = new UserAuto();
		String emailId = System.currentTimeMillis() + "@g.com";
		userAuto.setEmailId(emailId);

		return userAuto;

	}

}
