package com.bupo.dao;

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

}
