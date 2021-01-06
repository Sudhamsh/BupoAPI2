package com.reit.services;

import java.security.Key;

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

}
