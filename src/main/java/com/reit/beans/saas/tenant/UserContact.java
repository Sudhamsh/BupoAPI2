package com.reit.beans.saas.tenant;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserContact {
	private String username;
	private String firstName;
	private String lastName;
	private String email;
	private String mobile;
	private String officePhone;
}
