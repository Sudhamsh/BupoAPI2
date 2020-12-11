package com.bupo.beans;

import java.util.HashMap;
import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AgentQuote {
	// stores multiple quotes from an agent
	private Map<String, QuoteSummary> quotes = new HashMap<String, QuoteSummary>();
}
