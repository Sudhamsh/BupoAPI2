package com.reit.services;

import org.apache.commons.beanutils.BeanUtils;
import org.bson.types.ObjectId;
import org.junit.Assert;
import org.junit.Test;

import com.bupo.beans.User;
import com.bupo.beans.UserRequestBean;
import com.bupo.services.UserService;
import com.bupo.services.UserServiceTest;
import com.reit.beans.PropertyBean;

public class SettingsServiceTest {
	private SettingService settingService = new SettingService();
	UserService userService = new UserService();

	@Test
	public void addFavorite_hp() {

		User user = new UserServiceTest().populateUserObject();

		try {

			// create user
			UserRequestBean userBean = new UserRequestBean();
			BeanUtils.copyProperties(userBean, user);
			ObjectId userObjID = userService.createUser(userBean);
			System.out.println("userObjID :" + userObjID);

			// create prop
			PropertyService propertyService = new PropertyService();
			PropertyBean propertyBean = new PropertyServiceTest().getDummyPropertyBean();
			ObjectId propObjectId = propertyService.createNewProperty(propertyBean);

			// add fav
			System.out.println("Email Id:" + user.getEmail());
			settingService.addFavorite(user.getEmail(), propObjectId.toHexString());

			User userFromDB = userService.getUserByEmail(user.getEmail());
			Assert.assertEquals("Fav's size is not 1", 1, userFromDB.getSettings().getFavProps().size());

		} catch (Exception e) {
			e.printStackTrace();
			Assert.fail("Create User failed ;)");
		}

	}

	@Test
	public void addNotes_hp() {

		User user = new UserServiceTest().populateUserObject();

		try {

			// create user
			UserRequestBean userBean = new UserRequestBean();
			BeanUtils.copyProperties(userBean, user);
			ObjectId userObjID = userService.createUser(userBean);
			System.out.println("userObjID :" + userObjID);

			// create prop
			PropertyService propertyService = new PropertyService();
			PropertyBean propertyBean = new PropertyServiceTest().getDummyPropertyBean();
			ObjectId propObjectId = propertyService.createNewProperty(propertyBean);

			// add fav
			System.out.println("Email Id:" + user.getEmail());
			settingService.addNotes(user.getEmail(), propObjectId.toHexString(), "Test Note 1");

			settingService.addNotes(user.getEmail(), propObjectId.toHexString(), "Test Note 2");
			User userFromDB = userService.getUserByEmail(user.getEmail());
			Assert.assertEquals("Notes size is not 1", 1, userFromDB.getSettings().getPropNotes().size());

		} catch (Exception e) {
			e.printStackTrace();
			Assert.fail("Create User failed ;)");
		}

	}

}
