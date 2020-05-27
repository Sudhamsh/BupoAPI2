package com.bupo.services;

import java.io.File;
import java.io.IOException;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

import com.google.common.base.Preconditions;
import com.google.common.base.Strings;

public class PdfReaderService {

	public String readFile(String fileName) throws IOException {
		Preconditions.checkArgument(!Strings.isNullOrEmpty(fileName), "File name is null or empty");
		String text = null;
		PDDocument document = null;
		try {
			System.out.println("fileName : " + fileName);
			document = PDDocument.load(new File(fileName));
			if (!document.isEncrypted()) {

				// document.getDocumentInformation().getMetadataKeys()
				PDFTextStripper stripper = new PDFTextStripper();
				text = stripper.getText(document);
				System.out.println("Text:" + text);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (document != null) {
				document.close();
			}
		}

		return text;

	}

}
