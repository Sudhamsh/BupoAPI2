package com.bupo.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import com.bupo.dao.model.UserAuto;
import com.bupo.util.AutoUserTestUtil;

public class BaseDaoTest {
	@Test
	public void saveTest() {
		BaseDao baseDao = new BaseDao();
		UserAuto userAuto = AutoUserTestUtil.createUser();

		baseDao.create(userAuto);

		baseDao.delete(userAuto);
	}

	@Test
	public void updateTest() {
		BaseDao baseDao = new BaseDao();
		UserAuto userAuto = AutoUserTestUtil.createUser();

		baseDao.create(userAuto);
		userAuto.setFirstName("Sudhamsh");
		baseDao.update(UserAuto.class, userAuto);

		baseDao.delete(userAuto);
	}

	@Test
	public void findByNamedQuery() {
		try {
			BaseDao baseDao = new BaseDao();
			Map<String, String> paramsMap = new HashMap<String, String>();
			paramsMap.put("code", "1VP7Y-AZ32G-YTW7P-8BFA2");
			paramsMap.put("lastName", "bachu");
			paramsMap.put("zip", "40000");

			List<UserAuto> results = baseDao.findByNamedQuery("findAutoByCodeByLnameByZip", paramsMap, UserAuto.class);
			System.out.println(results.size());
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
