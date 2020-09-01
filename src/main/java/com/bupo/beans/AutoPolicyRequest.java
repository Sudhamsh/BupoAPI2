package com.bupo.beans;

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
	private String zip;
	private String phone;
	private String email;
	private List<Vehicle> vehicles;
	private List<AutoDriver> drivers;
	private List<Coverage> coverages;

}
