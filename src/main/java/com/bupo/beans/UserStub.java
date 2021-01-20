package com.bupo.beans;

import com.reit.enums.AuthType;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class UserStub {
	private String password;
	private AuthType authType;
	private String token;
	private String name;
	private String email;
	private String pictureUrl;
	private String locale;
	private String familyName;
	private String givenName;
	private String role;
	private String mobile;
	private String landline;
	private boolean isPasswordTemp;

}
