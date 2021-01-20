package com.reit.services;

import static com.mongodb.client.model.Filters.eq;

import java.security.Key;
import java.util.List;

import org.bson.conversions.Bson;

import com.bupo.dao.MongoDao;
import com.bupo.enums.MongoCollEnum;
import com.google.common.base.Preconditions;
import com.google.gson.Gson;
import com.mongodb.client.model.Aggregates;
import com.reit.beans.TokenBean;

import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class TokenService {

	private final String subject = "D0ntA5kM5";
	private final Key key = Keys.secretKeyFor(SignatureAlgorithm.HS256);

	Gson gson = new Gson();

	public String createNewToken(String email) {
		String token = Jwts.builder().setSubject(subject).signWith(key).compact();

		// store token in DB
		TokenBean tokenBean = new TokenBean(token, email);

		new MongoDao().insert(MongoCollEnum.TOKEN.toString(), gson.toJson(tokenBean));
		return token;
	}

	public TokenBean lookupToken(String token) {
		TokenBean tokenBean = null;

		if (!isTokenValid(token)) {

		}

		MongoDao mongoDao = new MongoDao();
		Bson filter = eq("token", token);

		List<TokenBean> results = mongoDao.aggregate(MongoCollEnum.TOKEN.toString(), TokenBean.class,
				Aggregates.match(filter), 1);

		if (results != null && results.size() > 0) {
			tokenBean = results.get(0);
		}

		return tokenBean;
	}

	public String getTenantName() {
		return "dev2_org";
	}

	public String getLoggedInUser() {
		return "a@a.com";
	}

	public boolean isTokenValid(String token) {
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
