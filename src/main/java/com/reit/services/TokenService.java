package com.reit.services;

import java.security.Key;

import com.google.common.base.Preconditions;

import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class TokenService {

	private final String subject = "D0ntA5kM5";
	private final Key key = Keys.secretKeyFor(SignatureAlgorithm.HS256);
	private String token = null;

	TokenService(String token) {
		this.token = token;
	}

	public String getToken() {
		token = Jwts.builder().setSubject(subject).signWith(key).compact();

		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getTenantName() {
		return "reitCo";
	}

	public String getLoggedInUser() {
		return "a@a.com";
	}

	public boolean isTokenValid() {
		Preconditions.checkNotNull(token, "Token is null");

		boolean isValid = false;

		try {
			Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
			isValid = true;
		} catch (JwtException e) {
			isValid = false;
		}

		return isValid;
	}

}
