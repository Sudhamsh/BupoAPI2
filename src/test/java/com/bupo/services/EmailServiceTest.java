package com.bupo.services;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

public class EmailServiceTest {
	@Test
	public void sendEmail() {
		EmailService emailService = new EmailService();
		Map<String, String> templateValues = new HashMap<String, String>();
		templateValues.put("REF_CODE", "1VP7Y-AZ32G-YTW7P-8BFA2");
		templateValues.put("SEARCH_QUOTE_LINK", "<a href='#'>view</a>");

		emailService.sendMail("onesearchbuy@gmail.com", "emailTemplates/refCodeTemplate.html", templateValues);
	}

}
