package com.reit.services;

import java.security.Key;

import org.junit.Assert;
import org.junit.Test;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

public class TokenServiceTest {

	@Test
	public void getToken_hp() {
		TokenService tokenService = new TokenService();
		String token = tokenService.createNewToken("a@a.com");
		Assert.assertNotNull(token);
	}

	@Test
	public void lookupToken_hp() {
		TokenService tokenService = new TokenService();
		String email = "a@a.com";
		String token = tokenService.createNewToken(email);
		Assert.assertNotNull(tokenService.lookupToken(token));
	}

	@Test
	public void isTokenValid_hp() {
		try {
			TokenService tokenService = new TokenService();
			String token = tokenService.createNewToken("b@b.com");
			tokenService.isTokenValid(token);
		} catch (Exception e) {
			Assert.fail(e.getMessage());
		}

	}

	@Test
	public void isTokenValid_cc() {
		try {
			String token = null;
			TokenService tokenService = new TokenService();
			tokenService.isTokenValid(token);
			Assert.fail("Invalid token came as valid");
		} catch (Exception e) {

		}

		try {
			TokenService tokenService = new TokenService();
			tokenService.isTokenValid(null);
			Assert.fail("Invalid token came as valid");
		} catch (Exception e) {

		}

		try {

			Key key = Keys.secretKeyFor(SignatureAlgorithm.HS256);
			TokenService tokenService = new TokenService();
			String token = Jwts.builder().setSubject("abc").signWith(key).compact();
			boolean isValid = tokenService.isTokenValid(token);
			Assert.assertFalse("Invalid token came as valid", isValid);
		} catch (Exception e) {

		}

	}

}
