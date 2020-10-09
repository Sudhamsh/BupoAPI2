package com.bupo.services;

import org.bson.Document;
import org.junit.Assert;
import org.junit.Test;

import com.bupo.beans.User;
import com.google.gson.JsonObject;

public class UserServiceTest {
	UserService userService = new UserService();

	@Test
	public void createUser_hp() {

		User user = populateUserObject();

		try {
			userService.createUser(user);
		} catch (Exception e) {
			Assert.fail("Create User failed ;)");
		}

	}

	private User populateUserObject() {

		User user = new User();
		user.setDisplayName("displayName");
		user.setEmail(System.currentTimeMillis() + "@a.com");
		user.setPasswordTemp(true);
		user.setSettings(new JsonObject());

		return user;

	}

	@Test
	public void createUser_cs() {

		try {
			userService.createUser(null);
			Assert.fail("Create User accepted null object");
		} catch (Exception e) {

		}

	}

	@Test
	public void getUser_hp() {

		try {
			// create
			User user = populateUserObject();
			userService.createUser(user);

			// find
			Document userDoc = userService.getUserByEmail(user.getEmail());
			System.out.println("userDoc: " + userDoc);

		} catch (Exception e) {
			Assert.fail("Failed to find user, who was just created ;)");
		}

	}

	@Test
	public void getUser_cs() {

		try {

			// find
			Document userDoc = userService.getUserByEmail(System.currentTimeMillis() + "@a.com");
			System.out.println("userDoc: " + userDoc);
			Assert.assertTrue("Found user with a random email. Coincidence ?", userDoc == null);

		} catch (Exception e) {
			Assert.fail("Exception during user search. " + e.getMessage());
		}

	}

	@Test
	public void updateUser_cs() {

	}

	@Test
	public void updateUser_hp() {
		String newDispName = "NewDisplayName";
		try {
			// create
			User user = populateUserObject();
			userService.createUser(user);

			// update
			user.setDisplayName(newDispName);
			userService.updateUser(user);

			// find and validate
			Document userDoc = userService.getUserByEmail(user.getEmail());
			System.out.println(userDoc);
			Assert.assertTrue("Failed to update the displayName in user object",
					newDispName.equals(userDoc.getString("displayName")));
		} catch (Exception e) {
			e.printStackTrace();
			Assert.fail("Exception during user update. " + e.getMessage());
		}
	}

}
