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
	private String description;
	private int deductable;
	private int premiumAmount;
	private String limits; // Ex: 100K/300K

}
