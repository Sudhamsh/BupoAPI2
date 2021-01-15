package com.reit.services;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

public class GenerateDocServiceTest {
	GenerateDocService generateDocService = new GenerateDocService();

	@Test
	public void generateDoc_hp() {
		Map<String, String> variablesMap = new HashMap<>();
		variablesMap.put("purchasePrice", "1,000,000");
		variablesMap.put("loi_amount", "100,000");

		generateDocService.generateDoc("dev", "familyDollar8", "LOI", variablesMap);
	}

}
