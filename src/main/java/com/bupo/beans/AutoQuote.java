package com.bupo.beans;

import com.bupo.enums.QuoteType;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AutoQuote {
	private int bilPremium;
	private int pdlPremium;
	private int mpPremium;
	private int uniPremium;
	private String bodilyInjuryLimit;
	private String propDamLiability;
	private String medicalPayments;
	private String uninsuredMotorist;
	private QuoteType quoteType; // Stores least, recommended or expensive quote enums

}
