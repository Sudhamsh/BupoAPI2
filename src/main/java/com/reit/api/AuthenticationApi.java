package com.reit.api;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;
import static javax.ws.rs.core.Response.ok;
import static javax.ws.rs.core.Response.status;
import static org.apache.logging.log4j.util.Strings.isBlank;

import javax.naming.AuthenticationException;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.bupo.beans.UserRequestBean;
import com.bupo.services.UserService;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.reit.beans.AuthResponse;
import com.reit.services.TokenService;

@Path("/auth")
public class AuthenticationApi {
	private Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();

	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response authenticateUser(UserRequestBean authRequestBean) {

		try {

			// Authenticate the user using the credentials provided
			authenticate(authRequestBean);

			// Issue a token for the user
			String token = issueToken(authRequestBean.getEmail());

			// Return the token on the response
			return ok(gson.toJson(new AuthResponse(token))).build();

		} catch (Exception e) {
			return status(Response.Status.FORBIDDEN).build();
		}
	}

	private void authenticate(UserRequestBean authBean) throws Exception {
		checkNotNull(authBean, "UserAuthRequestBean  is null");
		checkArgument(!(isBlank(authBean.getPassword()) && isBlank(authBean.getToken())),
				"Password and token are both is null, can't authenticate");

		UserService userService = new UserService();
		if (!isBlank(authBean.getPassword())) {
			if (!userService.isUserValid(authBean.getEmail(), authBean.getPassword())) {
				throw new AuthenticationException("Login failed");
			}
		} else if (!isBlank(authBean.getPassword())) {
			UserRequestBean userFromToken = userService.getUserWithThirdPartyToken(authBean);
			userService.upsertUser(userFromToken);
		}
	}

	private String issueToken(String email) {
		// Issue a token (can be a random String persisted to a database or a JWT token)
		// The issued token must be associated to a user
		// Return the issued token

		return new TokenService().createNewToken(email);
	}
}
