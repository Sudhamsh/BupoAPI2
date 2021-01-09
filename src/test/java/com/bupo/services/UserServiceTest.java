package com.bupo.services;

import java.lang.reflect.InvocationTargetException;

import org.apache.commons.beanutils.BeanUtils;
import org.junit.Assert;
import org.junit.Test;

import com.bupo.beans.User;
import com.bupo.beans.UserRequestBean;
import com.bupo.exceptions.ObjectExists;

public class UserServiceTest {
	UserService userService = new UserService();

	@Test
	public void createUser_hp() {

		User user = populateUserObject();

		try {
			UserRequestBean userBean = new UserRequestBean();
			BeanUtils.copyProperties(userBean, user);
			userService.createUser(userBean);
		} catch (Exception e) {
			e.printStackTrace();
			Assert.fail("Create User failed ;)");
		}

	}

	public User populateUserObject() {

		User user = new User();
		user.setDisplayName("displayName");
		user.setEmail(System.currentTimeMillis() + "@a.com");
		user.setPassword(String.valueOf(System.currentTimeMillis()));

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
			UserRequestBean userBean = new UserRequestBean();
			userService.createUser(userBean);

			// find
			User userDoc = userService.getUserByEmail(user.getEmail());
			System.out.println("userDoc: " + userDoc);

		} catch (Exception e) {
			Assert.fail("Failed to find user, who was just created ;)");
		}

	}

	@Test
	public void getUser_cc() {

		try {

			// find
			User userDoc = userService.getUserByEmail(System.currentTimeMillis() + "@a.com");
			System.out.println("userDoc: " + userDoc);
			Assert.assertTrue("Found user with a random email. Coincidence ?", userDoc == null);

		} catch (Exception e) {
			Assert.fail("Exception during user search. " + e.getMessage());
		}

	}

	@Test
	public void updateUser_cc() {

	}

	@Test
	public void updateUser_hp() {
		String newDispName = "NewDisplayName";
		try {
			// create
			User user = populateUserObject();
			UserRequestBean userBean = new UserRequestBean();
			userService.createUser(userBean);

			// update
			user.setDisplayName(newDispName);
			userService.updateUser(user);

			// find and validate
			User userDoc = userService.getUserByEmail(user.getEmail());
			System.out.println(user);
			Assert.assertTrue("Failed to update the displayName in user object",
					newDispName.equals(userDoc.getDisplayName()));
		} catch (Exception e) {
			e.printStackTrace();
			Assert.fail("Exception during user update. " + e.getMessage());
		}
	}

	@Test
	public void isUserValid_hp() throws ObjectExists, IllegalAccessException, InvocationTargetException {

		// create
		User user = populateUserObject();

		UserRequestBean userBean = new UserRequestBean();
		BeanUtils.copyProperties(userBean, user);
		userService.createUser(userBean);

		// check if the user is valid
		boolean isUserValid = userService.isUserValid(user.getEmail(), user.getPassword());

		Assert.assertTrue("Newly created user came as invalid.", isUserValid);

	}

	@Test
	public void isUserValid_cc() {
		// check if the user is valid
		boolean isUserValid = userService.isUserValid("a@a.com", String.valueOf(System.currentTimeMillis()));

		Assert.assertFalse("Random credentials worked ;)", isUserValid);

		try {
			userService.isUserValid(null, String.valueOf(System.currentTimeMillis()));
			Assert.fail("Didn't get an exception on a null user find");
		} catch (Exception e) {
			// TODO: handle exception
		}

	}

}
