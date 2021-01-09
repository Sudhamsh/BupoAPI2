package com.reit.services;

import java.security.Key;

import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

public class TokenService {

	private final String subject = "D0ntA5kM5";
	private final Key key = Keys.secretKeyFor(SignatureAlgorithm.HS256);

	public String getToken() {
		String token = null;
		token = Jwts.builder().setSubject(subject).signWith(key).compact();

		return token;
	}

	public boolean isTokenValid(String token) {
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
