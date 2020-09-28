package com.bupo.beans;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@JsonInclude(Include.NON_EMPTY)
@JsonIgnoreProperties(ignoreUnknown = true)
public class AutoPolicyRequest {
	private String firstName;
	private String lastName;
	private String phone;
	private String email;
	private String homeOwnership;

	private Address address = new Address();

	private List<Vehicle> vehicles = new ArrayList<Vehicle>();
	private List<AutoDriver> drivers = new ArrayList<AutoDriver>();
	private Coverage coverage = new Coverage();

	// Used for updates
	private String code;

}
