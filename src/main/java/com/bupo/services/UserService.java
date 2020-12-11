package com.bupo.services;

import static com.mongodb.client.model.Filters.and;
import static com.mongodb.client.model.Filters.eq;

import org.apache.commons.beanutils.BeanUtils;
import org.bson.Document;
import org.bson.conversions.Bson;

import com.bupo.beans.User;
import com.bupo.beans.UserRequestBean;
import com.bupo.dao.MongoDao;
import com.bupo.enums.MongoCollEnum;
import com.bupo.util.LogManager;
import com.google.common.base.Preconditions;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class UserService {
	private LogManager logger = LogManager.getLogger(this.getClass());
	MongoDao mongoDao = new MongoDao();
	private Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();

	/**
	 * Create a new user in DB
	 * 
	 * @param user
	 */
	public void createUser(UserRequestBean userBean) {
		Preconditions.checkNotNull(userBean, "User Object is null");
		Preconditions.checkNotNull(userBean.getEmail(), "Email in User Obj is null");
		try {
			if (getUserByEmail(userBean.getEmail()) != null) {
				throw new RuntimeException("User with email :" + userBean.getEmail() + " exists in DB");
			}
			User user = new User();
			BeanUtils.copyProperties(user, userBean);

			mongoDao.insert(MongoCollEnum.User.toString(), gson.toJson(user));
		} catch (Exception e) {
			logger.error(e);
			throw new RuntimeException(e);
		}
	}

	public boolean isUserValid(String email, String password) {
		Preconditions.checkNotNull(email, "Email is null");
		Preconditions.checkNotNull(password, "Password is null");
		boolean isValid = false;

		MongoDao mongoDao = new MongoDao();
		Bson filter = and(eq("email", email), eq("password", password));
		Document userDoc = mongoDao.findOne(MongoCollEnum.User.toString(), filter);
		if (userDoc != null) {
			isValid = true;
		}

		return isValid;
	}

	public Document getUserByEmail(String email) {

		MongoDao mongoDao = new MongoDao();
		Bson filter = eq("email", email);
		Document user = mongoDao.findOne(MongoCollEnum.User.toString(), filter);

		return user;

	}

	public void updateUser(UserRequestBean userBean) {
		Preconditions.checkNotNull(userBean, "User is null");
		Preconditions.checkNotNull(userBean.getEmail(), "User Email is null");
		MongoDao mongoDao = new MongoDao();

		try {
			User user = new User();
			BeanUtils.copyProperties(user, userBean);
			mongoDao.findAndReplace(MongoCollEnum.User.toString(), eq("email", user.getEmail()), gson.toJson(user));
		} catch (Exception e) {
			logger.error(e);
			throw new RuntimeException(e);
		}
	}

	public void deleteUser() {

	}

}
