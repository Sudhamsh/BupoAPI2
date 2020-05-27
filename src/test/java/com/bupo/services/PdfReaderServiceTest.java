package com.bupo.services;

import org.junit.Assert;
import org.junit.Test;

public class PdfReaderServiceTest {
	PdfReaderService pdfReaderService = new PdfReaderService();

	@Test
	public void readFile() {
		try {
			pdfReaderService.readFile(null);
			Assert.assertTrue("Readfile message accepted null", false);
		} catch (Exception e) {

		}

		try {
			pdfReaderService.readFile("");
			Assert.assertTrue("Readfile message accepted empty string", false);
		} catch (Exception e) {

		}

		try {
			pdfReaderService
					.readFile("/Users/sudhamshbachu/Documents/GitHub/BupoApi/src/test/resources/PGRDeclarations.pdf");

		} catch (Exception e) {

		}

	}

}
