package com.bupo.beans;

import com.bupo.enums.EducationLevel;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
@AllArgsConstructor
@NoArgsConstructor
public class AutoDriver extends Person {

	private int yearsLicensed;
	private EducationLevel educationLevel;

}
