package com.bupo.beans;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class Coverage {
	private String description;
	private int deductable;
	private int premiumAmount;
	private String limits; // Ex: 100K/300K

}
