package com.reit.api;

import java.util.List;

import javax.validation.constraints.NotNull;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import com.bupo.beans.User;
import com.bupo.beans.UserRequestBean;
import com.bupo.exceptions.ObjectExists;
import com.bupo.services.UserService;
import com.google.common.base.Preconditions;
import com.google.gson.Gson;
import com.reit.beans.AuthResponse;
import com.reit.services.TokenService;
import com.reit.util.GsonUtils;
import com.reit.util.Secured;

@Path("/user")
public class UserApi {
	private UserService userService = new UserService();
	private Gson gson = GsonUtils.getGson();
	private static final String AUTHENTICATION_SCHEME = "Bearer";

	@POST
	@Consumes({ MediaType.APPLICATION_JSON })
	public Response createUser(UserRequestBean user) {
		Response response = null;

		try {
			userService.createUser(user);

			// Issue a token for the user
			String token = new TokenService().createNewToken(user.getEmail());

			// Return the token on the response
			response = Response.status(201).entity(gson.toJson(new AuthResponse(token))).build();
		} catch (ObjectExists e) {
			e.printStackTrace();
			response = Response.status(Status.CONFLICT).entity(Status.NO_CONTENT).build();
		} catch (Exception e) {
			e.printStackTrace();
			response = Response.serverError().build();
		}

		return response;

	}

	@GET
	@Produces({ MediaType.APPLICATION_JSON })
	@Secured
	public Response getTenantUsers() {
		Response response = null;
		try {
			List<User> users = userService.getLoggedInTenantUsers();

			response = Response.status(200).entity(gson.toJson(users)).build();

		} catch (Exception e) {
			response = Response.serverError().build();
		}

		return response;
	}

	@GET
	@Path("/login")
	@Produces({ MediaType.APPLICATION_JSON })
	public Response login(@NotNull(message = "API payload can't be null") UserRequestBean user) {
		Response response = null;
		Preconditions.checkNotNull(user.getEmail(), "User email is null in the payload");
		Preconditions.checkNotNull(user.getPassword(), "User password is null in the payload");
		try {
			boolean isValid = userService.isUserValid(user.getEmail(), user.getPassword());
			if (isValid) {
				response = Response.status(200).build();
			} else {
				response = Response.status(Status.UNAUTHORIZED).entity("{}").build();
			}
		} catch (Exception e) {
			response = Response.serverError().build();
		}

		return response;
	}

	@DELETE
	@Produces({ MediaType.APPLICATION_JSON })
	public Response logout(@HeaderParam(HttpHeaders.AUTHORIZATION) String authHeader) {
		Response response = null;

		try {
			String token = authHeader.substring(AUTHENTICATION_SCHEME.length()).trim();
			new TokenService().deleteToken(token);
			response = Response.status(200).build();
		} catch (Exception e) {
			e.printStackTrace();
			response = Response.serverError().build();
		}

		return response;
	}

}
