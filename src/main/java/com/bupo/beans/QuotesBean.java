package com.bupo.beans;

import java.util.HashMap;
import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class QuotesBean {
	private String code;

	private Map<String, AgentQuote> providerQuotes = new HashMap<String, AgentQuote>();

}
