package com.bupo.services;

import java.lang.reflect.InvocationTargetException;

import org.apache.commons.beanutils.BeanUtils;
import org.junit.Assert;
import org.junit.Test;

import com.bupo.beans.User;
import com.bupo.beans.UserRequestBean;
import com.bupo.exceptions.ObjectExists;
import com.reit.enums.AuthType;

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

	@Test
	public void getLoggedInTenantUsers() {
		Assert.assertTrue(" No users found for logged in tenant", userService.getLoggedInTenantUsers().size() > 0);
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
	public void isUserValid_tokenFlow_cc() throws ObjectExists, IllegalAccessException, InvocationTargetException {

		// create
		User user = populateUserObject();

		UserRequestBean userBean = new UserRequestBean();
		BeanUtils.copyProperties(userBean, user);
		userBean.setPassword(null);

		try {
			userService.createUser(userBean);
			Assert.fail("Token user creation flow failed");
		} catch (Exception e) {

		}

	}

	@Test
	public void isUserValid_tokenFlow_hp() throws ObjectExists, IllegalAccessException, InvocationTargetException {

		// create
		User user = populateUserObject();

		UserRequestBean userBean = new UserRequestBean();
		BeanUtils.copyProperties(userBean, user);
		userBean.setAuthType(AuthType.GOOGLE);
		userBean.setToken(
				"eyJhbGciOiJSUzI1NiIsImtpZCI6Ijc4M2VjMDMxYzU5ZTExZjI1N2QwZWMxNTcxNGVmNjA3Y2U2YTJhNmYiLCJ0eXAiOiJKV1QifQ.eyJpc3MiOiJhY2NvdW50cy5nb29nbGUuY29tIiwiYXpwIjoiNzYwNDY3MzM4NDQyLThtcWN1ZzcyczV2bjA3bmp0aG9iZGQ4dGI2ZXFrZWs4LmFwcHMuZ29vZ2xldXNlcmNvbnRlbnQuY29tIiwiYXVkIjoiNzYwNDY3MzM4NDQyLThtcWN1ZzcyczV2bjA3bmp0aG9iZGQ4dGI2ZXFrZWs4LmFwcHMuZ29vZ2xldXNlcmNvbnRlbnQuY29tIiwic3ViIjoiMTAxODM4MjQ4NDM3NDkyMjA4NTMzIiwiZW1haWwiOiJzdWRoYW1zaC5iQGdtYWlsLmNvbSIsImVtYWlsX3ZlcmlmaWVkIjp0cnVlLCJhdF9oYXNoIjoibVhRdkFVMThPdUVBbUxpMEc3aUZmZyIsIm5hbWUiOiJTdWRoYW1zaCBCYWNodSIsInBpY3R1cmUiOiJodHRwczovL2xoMy5nb29nbGV1c2VyY29udGVudC5jb20vYS0vQU9oMTRHZ2xqNFJiVnVYd0JNRllUOVprUGdWNi1oU3dSZkdwbE5mR056Q2E9czk2LWMiLCJnaXZlbl9uYW1lIjoiU3VkaGFtc2giLCJmYW1pbHlfbmFtZSI6IkJhY2h1IiwibG9jYWxlIjoiZW4iLCJpYXQiOjE2MTA3NzY2MzMsImV4cCI6MTYxMDc4MDIzMywianRpIjoiNDhlNzcxN2UzNmRhZjc4ZmYxYjZlNmRlNjJjNmRjOGFlMGI0YmRiNiJ9.3qpmqVMEE68Wiq8xKABzMH5EI08CLYu59GZso19yoGbRVA69wAJxym_pxF8ZLFzCWNISt_mpZKdk-QUDrCQk7TbDtxHSL9KS8jOkZS7bfTUVqs19NLOVcn2rtoeQEvSauyWR9BBfyteGLY-_Ub3ZsoD0-ZyoQyYlVTiZsJgF1eOpnA6Fm6khbocazKMtU9WkTeUdEyskEQrPmJGvNlfwDlyHLA-mOyt3_U8_QZ-tq1zNQ3YlBAkYfh5HyG44r_3dvjyZQ8SAN8WwM0al-egUDbo0FQZMRnM3_p_8HVUshqdHuO9p4bT2D_5jmdR2V6wMYwFU_ifKyVSfarnJc-Udsw");
		userBean.setPassword(null);

		try {
			userService.createUser(userBean);

		} catch (Exception e) {
			e.printStackTrace();
			Assert.fail("Token user creation flow failed");
		}

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
