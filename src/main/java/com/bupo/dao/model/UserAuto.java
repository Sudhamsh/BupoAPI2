package com.bupo.dao.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import com.bupo.beans.AutoDriver;
import com.bupo.beans.AutoPolicyRequest;
import com.google.gson.Gson;

import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "user_auto")
@NamedQueries({
		@NamedQuery(name = "findAutoByCodeByLnameByZip", query = "SELECT u FROM UserAuto as u WHERE u.code = :code AND u.lastName = :lastName AND u.zip = :zip"),
		@NamedQuery(name = "findAutoByCode", query = "SELECT u FROM UserAuto as u WHERE u.code = :code"),
		@NamedQuery(name = "findOpenRequests", query = "SELECT u FROM UserAuto u") })
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

	String quotes;
	String status;

	public void anonymizePersonalData() {

		email = null;
		firstName = null;
		lastName = null;

		Gson gson = new Gson();
		AutoPolicyRequest autoPolicyRequest = gson.fromJson(policyRequestDetails, AutoPolicyRequest.class);

		List<AutoDriver> driversAnonymized = new ArrayList<AutoDriver>();

		// Add only required fields.
		if (autoPolicyRequest != null && autoPolicyRequest.getDrivers() != null) {
			autoPolicyRequest.getDrivers().forEach(driver -> {
				AutoDriver tempDriver = new AutoDriver();
				tempDriver.setEducationLevel(driver.getEducationLevel());
				tempDriver.setMaritalStatus(driver.getMaritalStatus());
				driversAnonymized.add(tempDriver);
			});
			autoPolicyRequest.setDrivers(driversAnonymized);
		}

		policyRequestDetails = gson.toJson(autoPolicyRequest);

	}

}
