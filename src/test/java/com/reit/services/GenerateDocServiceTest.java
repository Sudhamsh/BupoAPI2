package com.reit.services;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

public class GenerateDocServiceTest {
	GenerateDocService generateDocService = new GenerateDocService();

	@Test
	public void generateDoc_hp() {
		Map<String, String> variablesMap = new HashMap<>();
		variablesMap.put("noi_amount", "100,000");
		variablesMap.put("purchasePrice", "1,000,000");

		try {
			generateDocService.generateDoc("dev", "familyDollar11", "LOI", variablesMap);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
