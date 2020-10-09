package com.bupo.services;

import static com.mongodb.client.model.Filters.eq;

import org.bson.Document;
import org.bson.conversions.Bson;

import com.bupo.beans.User;
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
	public void createUser(User user) {
		Preconditions.checkNotNull(user, "User Object is null");
		Preconditions.checkNotNull(user.getEmail(), "Email in User Obj is null");
		try {
			if (getUserByEmail(user.getEmail()) != null) {
				throw new RuntimeException("User with email :" + user.getEmail() + " exists in DB");
			}
			mongoDao.insert(MongoCollEnum.User.toString(), gson.toJson(user));
		} catch (Exception e) {
			logger.error(e);
			throw new RuntimeException(e);
		}
	}

	public Document getUserByEmail(String email) {

		MongoDao mongoDao = new MongoDao();
		Bson filter = eq("email", email);
		Document user = mongoDao.findOne(MongoCollEnum.User.toString(), filter);

		return user;

	}

	public void updateUser(User user) {
		Preconditions.checkNotNull(user, "User is null");
		Preconditions.checkNotNull(user.getEmail(), "User Email is null");
		MongoDao mongoDao = new MongoDao();

		try {
			mongoDao.findAndReplace(MongoCollEnum.User.toString(), eq("email", user.getEmail()), gson.toJson(user));
		} catch (Exception e) {
			logger.error(e);
			throw new RuntimeException(e);
		}
	}

	public void deleteUser() {

	}

}
