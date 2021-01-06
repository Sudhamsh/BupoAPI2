package com.reit.services;

import javax.ws.rs.NotAuthorizedException;

public class AuthorizationService {

	// TODO : Dummy impl for now
	public static boolean hasAuthorized() throws NotAuthorizedException {
		boolean isAuthorized = true;

		if (!isAuthorized) {
			throw new NotAuthorizedException("User not authorized for this opeartion");
		}

		return isAuthorized;
	}

}
