package com.bupo.beans;

import com.bupo.enums.QuoteType;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class QuoteSummary {
	private String detailId;
	private int monthlyPremiuim;
	private int singlePaymentPremiuim;
	private QuoteType quotetype;

}
