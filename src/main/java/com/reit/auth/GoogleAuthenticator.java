package com.reit.auth;

import java.util.Collections;

import javax.ws.rs.NotAuthorizedException;

import com.bupo.beans.UserRequestBean;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken.Payload;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.apache.ApacheHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;

public class GoogleAuthenticator implements Authenticator {
	private final static String CLIENT_ID = "760467338442-8mqcug72s5vn07njthobdd8tb6eqkek8.apps.googleusercontent.com";

	@Override
	public void isTokenValid(String idTokenString) throws Exception {

		GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(new ApacheHttpTransport(),
				new JacksonFactory())
						// Specify the CLIENT_ID of the app that accesses the backend:
						.setAudience(Collections.singletonList(CLIENT_ID))
						// Or, if multiple clients access the backend:
						// .setAudience(Arrays.asList(CLIENT_ID_1, CLIENT_ID_2, CLIENT_ID_3))
						.build();

		// (Receive idTokenString by HTTPS POST)

		GoogleIdToken idToken = verifier.verify(idTokenString);
		if (idToken == null) {
			throw new NotAuthorizedException("Invalid Token");
		}

	}

	@Override
	public UserRequestBean getUserWithToken(String idTokenString) throws Exception {

		UserRequestBean userRequestBean = new UserRequestBean();

		GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(new ApacheHttpTransport(),
				new JacksonFactory())
						// Specify the CLIENT_ID of the app that accesses the backend:
						.setAudience(Collections.singletonList(CLIENT_ID))
						// Or, if multiple clients access the backend:
						// .setAudience(Arrays.asList(CLIENT_ID_1, CLIENT_ID_2, CLIENT_ID_3))
						.build();

		// (Receive idTokenString by HTTPS POST)

		GoogleIdToken idToken = verifier.verify(idTokenString);

		if (idToken == null) {
			throw new NotAuthorizedException("Invalid Token");
		}

		Payload payload = idToken.getPayload();
		// Get profile information from payload
		userRequestBean.setEmail(payload.getEmail());

		userRequestBean.setName((String) payload.get("name"));
		userRequestBean.setPictureUrl((String) payload.get("picture"));
		userRequestBean.setLocale((String) payload.get("locale"));
		userRequestBean.setFamilyName((String) payload.get("family_name"));

		userRequestBean.setGivenName((String) payload.get("given_name"));

		return userRequestBean;
	}

}
