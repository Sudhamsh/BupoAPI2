package com.reit.services;

import org.junit.Test;

import io.jsonwebtoken.lang.Assert;

public class TokenServiceTest {
	private TokenService tokenService = new TokenService();

	@Test
	public void getToken_hp() {
		String token = tokenService.getToken();
		System.out.println(token);
		Assert.notNull(token);
	}

}
