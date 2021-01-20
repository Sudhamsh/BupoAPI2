package com.reit.auth;

import com.bupo.beans.UserRequestBean;

public interface Authenticator {
	void isTokenValid(String idTokenString) throws Exception;

	UserRequestBean getUserWithToken(String idTokenString) throws Exception;

}
