package com.bupo.api;

import javax.validation.constraints.NotNull;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import com.bupo.beans.User;
import com.bupo.beans.UserRequestBean;
import com.bupo.services.UserService;
import com.google.common.base.Preconditions;

@Path("/user")
public class UserApi {
	private UserService userService = new UserService();

	@POST
	@Consumes({ MediaType.APPLICATION_JSON })
	public Response createUser(@NotNull(message = "API payload can't be null") UserRequestBean user) {
		Response response = null;

		try {
			userService.createUser(user);

			response = Response.status(201).build();
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
				response = Response.status(Status.UNAUTHORIZED).build();
			}
		} catch (Exception e) {
			response = Response.serverError().build();
		}

		return response;
	}

	@GET
	@Path("/logout")
	@Produces({ MediaType.APPLICATION_JSON })
	public Response getAutoDetailsByCode(@NotNull(message = "API payload can't be null") User user) {
		Response response = null;

		try {

			response = Response.status(201).build();
		} catch (Exception e) {
			response = Response.serverError().build();
		}

		return response;
	}

}
