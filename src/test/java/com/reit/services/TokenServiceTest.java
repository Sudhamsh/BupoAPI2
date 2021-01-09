package com.reit.services;

import java.security.Key;

import org.junit.Assert;
import org.junit.Test;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

public class TokenServiceTest {
	private TokenService tokenService = new TokenService();

	@Test
	public void getToken_hp() {
		String token = tokenService.getToken();
		Assert.assertNotNull(token);
	}

	@Test
	public void isTokenValid_hp() {
		try {
			String token = tokenService.getToken();
			tokenService.isTokenValid(token);
		} catch (Exception e) {
			Assert.fail(e.getMessage());
		}

	}

	@Test
	public void isTokenValid_cc() {
		try {
			String token = null;
			tokenService.isTokenValid(token);
			Assert.fail("Invalid token came as valid");
		} catch (Exception e) {

		}

		try {
			tokenService.isTokenValid("");
			Assert.fail("Invalid token came as valid");
		} catch (Exception e) {

		}

		try {
			Key key = Keys.secretKeyFor(SignatureAlgorithm.HS256);
			boolean isValid = tokenService.isTokenValid(Jwts.builder().setSubject("abc").signWith(key).compact());
			Assert.assertFalse("Invalid token came as valid", isValid);
		} catch (Exception e) {

		}

	}

}
