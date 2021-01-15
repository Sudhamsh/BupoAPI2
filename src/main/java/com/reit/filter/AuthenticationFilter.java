package com.reit.filter;

import java.security.Principal;

import javax.annotation.Priority;
import javax.ws.rs.Priorities;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import javax.ws.rs.ext.Provider;

import com.reit.util.Secured;

import io.jsonwebtoken.io.IOException;

@Secured
@Provider
@Priority(Priorities.AUTHENTICATION)
public class AuthenticationFilter implements ContainerRequestFilter {

	private static final String REALM = "example";
	private static final String AUTHENTICATION_SCHEME = "Bearer";

	@Override
	public void filter(ContainerRequestContext requestContext) throws IOException {
		System.out.println(" Request filter........");

		// Get the Authorization header from the request
		String authorizationHeader = requestContext.getHeaderString(HttpHeaders.AUTHORIZATION);
		String username = "abcd";

		// Validate the Authorization header
		if (!isTokenBasedAuthentication(authorizationHeader)) {
			abortWithUnauthorized(requestContext);

			return;
		}

		// Extract the token from the Authorization header
		String token = authorizationHeader.substring(AUTHENTICATION_SCHEME.length()).trim();

		try {

			// Validate the token
			validateToken(token);

		} catch (Exception e) {
			abortWithUnauthorized(requestContext);
		}

		final SecurityContext currentSecurityContext = requestContext.getSecurityContext();
		requestContext.setSecurityContext(new SecurityContext() {

			@Override
			public Principal getUserPrincipal() {
				return () -> username;
			}

			@Override
			public boolean isUserInRole(String role) {
				return true;
			}

			@Override
			public boolean isSecure() {
				return currentSecurityContext.isSecure();
			}

			@Override
			public String getAuthenticationScheme() {
				return AUTHENTICATION_SCHEME;
			}
		});
	}

	private boolean isTokenBasedAuthentication(String authorizationHeader) {

		// Check if the Authorization header is valid
		// It must not be null and must be prefixed with "Bearer" plus a whitespace
		// The authentication scheme comparison must be case-insensitive
		return authorizationHeader != null
				&& authorizationHeader.toLowerCase().startsWith(AUTHENTICATION_SCHEME.toLowerCase() + " ");
	}

	private void abortWithUnauthorized(ContainerRequestContext requestContext) {

		// Abort the filter chain with a 401 status code response
		// The WWW-Authenticate header is sent along with the response
		requestContext.abortWith(Response.status(Response.Status.UNAUTHORIZED)
				.header(HttpHeaders.WWW_AUTHENTICATE, AUTHENTICATION_SCHEME + " realm=\"" + REALM + "\"").build());
	}

	private void validateToken(String token) throws Exception {
		// Check if the token was issued by the server and if it's not expired
		// Throw an Exception if the token is invalid
	}

}