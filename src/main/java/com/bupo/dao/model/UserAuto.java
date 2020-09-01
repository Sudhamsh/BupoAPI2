package com.bupo.dao.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "user_auto")
@NamedQuery(name = "findAutoByCodeByLnameByZip", query = "SELECT u FROM UserAuto as u WHERE u.code = :code AND u.lastName = :lastName AND u.zip = :zip")
@Getter
@Setter
public class UserAuto {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	int id;
	String code;
	String email;
	@Column(name = "first_name")
	String firstName;
	@Column(name = "last_name")
	String lastName;
	String zip;
	@Column(name = "policy_details_file") // TODO: Find better way to store file content, DB is not the best place.
	String policyDetailsFile;

	@Column(name = "policy_request_details")
	String policyRequestDetails;

	@Column(name = "policy_urls")
	String policyUrls;

}
