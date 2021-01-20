package com.bupo.services;

import static com.mongodb.client.model.Filters.and;
import static com.mongodb.client.model.Filters.eq;

import java.util.List;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.logging.log4j.util.Strings;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;

import com.bupo.beans.User;
import com.bupo.beans.UserRequestBean;
import com.bupo.dao.MongoDao;
import com.bupo.enums.MongoCollEnum;
import com.bupo.exceptions.ObjectExists;
import com.bupo.util.LogManager;
import com.google.common.base.Preconditions;
import com.google.gson.Gson;
import com.mongodb.client.model.Aggregates;
import com.reit.auth.Authenticator;
import com.reit.auth.GoogleAuthenticator;
import com.reit.beans.saas.tenant.SaasTenantBean;
import com.reit.services.SaasTenantService;
import com.reit.services.TokenService;
import com.reit.util.GsonUtils;

public class UserService {
	private LogManager logger = LogManager.getLogger(this.getClass());
	MongoDao mongoDao = new MongoDao();
	private Gson gson = GsonUtils.getGson();

	/**
	 * Create a new user in DB
	 * 
	 * @param user
	 */
	public ObjectId createUser(UserRequestBean userBean) throws ObjectExists {
		Preconditions.checkNotNull(userBean, "User Object is null");
		Preconditions.checkNotNull(userBean.getEmail(), "Email in User Obj is null");

		ObjectId objectId = null;
		try {

			if (Strings.isNotBlank(userBean.getToken())) {
				// Overrider user from token as we can't trust the payload
				userBean = getUserWithThirdPartyToken(userBean);
			} else if (Strings.isBlank(userBean.getPassword()) && Strings.isBlank(userBean.getToken())) {
				// Admin is creating the account
				userBean.setPasswordTemp(true);
			}

			User user = new User();
			// Link user with tenant
			SaasTenantBean tenantBean = new SaasTenantService().getSaasTenant(new TokenService().getTenantName());
			user.setTenantObjId(tenantBean.getId().toHexString());
			BeanUtils.copyProperties(user, userBean);

			if (getUserByEmail(user.getEmail()) != null) {
				throw new ObjectExists("User with email :" + userBean.getEmail() + " exists in DB");
			}
			objectId = mongoDao.insert(MongoCollEnum.User.toString(), gson.toJson(user));
		} catch (ObjectExists e) {
			logger.error(e);
			throw new ObjectExists(e.getMessage());
		} catch (Exception e) {
			logger.error(e);
			throw new RuntimeException(e);
		}

		return objectId;
	}

	public void upsertUser(UserRequestBean userBean) {
		try {
			createUser(userBean);
		} catch (Exception e) {
			// Ignore exception as we want to create user when the user doesn't exists
		}

	}

	public UserRequestBean getUserWithThirdPartyToken(final UserRequestBean userBean) throws Exception {
		UserRequestBean userBeanFromToken = null;
		switch (userBean.getAuthType()) {
		case GOOGLE:
			Authenticator authenticator = new GoogleAuthenticator();
			userBeanFromToken = authenticator.getUserWithToken(userBean.getToken());
			break;
		default:
			throw new Exception("Token auth type not supported");
		}

		return userBeanFromToken;

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

	public User getUserByEmail(String email) {

		MongoDao mongoDao = new MongoDao();
		Bson filter = eq("email", email);
		User user = null;

		List<User> results = mongoDao.aggregate(MongoCollEnum.User.toString(), User.class, Aggregates.match(filter), 1);

		if (results != null && results.size() > 0) {
			user = results.get(0);
		}

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

	public List<User> getLoggedInTenantUsers() {
		String loogedInTenant = new TokenService().getTenantName();
		Preconditions.checkNotNull(loogedInTenant, "Tenant Name is null");
		SaasTenantBean tenantBean = new SaasTenantService().getSaasTenant(loogedInTenant);
		ObjectId tenantObjId = tenantBean.getId();
		MongoDao mongoDao = new MongoDao();
		Bson filter = eq("tenantObjId", tenantObjId.toHexString());

		List<User> results = mongoDao.aggregate(MongoCollEnum.User.toString(), User.class, Aggregates.match(filter),
				500);
		System.out.println("Tenant Users : " + results.size());

		return results;
	}

}
