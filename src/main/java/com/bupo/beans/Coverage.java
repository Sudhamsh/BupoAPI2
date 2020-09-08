package com.bupo.beans;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class Coverage {
	private String bodilyInjuryLimit;
	private String propDamLiability;
	private String medicalPayments;
	private String uninsuredMotorist;

}
