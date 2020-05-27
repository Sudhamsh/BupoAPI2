package com.bupo.dao.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

import lombok.Getter;
import lombok.Setter;

@Entity(name = "user_auto")
@Getter
@Setter
public class UserAuto {

	@Id
	String emailId;
	@Column(name = "first_name")
	String firstName;
	@Column(name = "last_name")
	String lastName;
	@Column(name = "policy_details") // TODO: Find better way to store file content, DB is not the best palce.
	String policyDetails;

	@Column(name = "policy_urls")
	String policyUrls;

}
