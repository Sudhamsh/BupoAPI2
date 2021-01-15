package com.reit.services;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;

import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.hwpf.usermodel.CharacterRun;
import org.apache.poi.hwpf.usermodel.Paragraph;
import org.apache.poi.hwpf.usermodel.Range;
import org.apache.poi.hwpf.usermodel.Section;
import org.apache.poi.xwpf.usermodel.XWPFDocument;

import com.bupo.util.LogManager;
import com.google.common.base.Preconditions;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class GenerateDocService {
	private LogManager logger = LogManager.getLogger(this.getClass());
	private Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();
	private static final String outputDir = "/Users/sudhamshbachu/eclipse-workspace-new/BupoAPI2/src/main/resources/out/";

	public String generateDoc(String saasTenantName, String propertyAddress, String templateName,
			Map<String, String> variablesMap) throws Exception {
		Preconditions.checkNotNull(saasTenantName, "Saas TenantName is null");
		Preconditions.checkNotNull(propertyAddress, "Property Address is null");
		Preconditions.checkNotNull(templateName, "Template Name is null");
		Preconditions.checkNotNull(variablesMap, "Variables Map is null");
		String seperator = "REIT";

		String populatedFileName = null;
		try {
			String resourcePath = getTempalteFilePath(templateName);
			Path templatePath = Paths.get(GenerateDocService.class.getClassLoader().getResource(resourcePath).toURI());
			XWPFDocument doc = new XWPFDocument(Files.newInputStream(templatePath));

			// loop through variables, could use some optimization
			for (String label : variablesMap.keySet()) {
				doc = replaceTextFor(doc, label, variablesMap.get(label));
			}

			new DropBoxService().createDocument(propertyAddress, templateName, doc);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e);
			throw new Exception(e);
		}

		return populatedFileName;

	}

	public static String getTempalteFilePath(String templateName) {
		String filePath = null;

		switch (templateName) {
		case "LOI":
			filePath = "reitTemplates/LOI_template.docx";
			break;

		default:
			break;
		}

		return filePath;
	}

	public static void main(String[] args) throws URISyntaxException, IOException {
		String resourcePath = "reitTemplates/LOI_template.docx";
		Path templatePath = Paths.get(GenerateDocService.class.getClassLoader().getResource(resourcePath).toURI());
		XWPFDocument doc = new XWPFDocument(Files.newInputStream(templatePath));
		doc = replaceTextFor(doc, "purchasePrice", "1,000,000");
		doc = replaceTextFor(doc, "loi_amount", "100,000");
		saveWord("reitTemplates/LOI_template_1.docx", doc);
	}

	private static XWPFDocument replaceTextFor(XWPFDocument doc, String findText, String replaceText) {
		doc.getParagraphs().forEach(p -> {
			p.getRuns().forEach(run -> {
				String text = run.text();
				System.out.println(text);
				if (text.contains(findText)) {
					run.setText(text.replace(findText, replaceText), 0);
				}
			});
		});

		return doc;
	}

	// TODO: Files should be stored in an external storage
	private static void saveWord(String fileName, XWPFDocument doc) throws FileNotFoundException, IOException {
		FileOutputStream out = null;

		try {

			File newFile = new File(outputDir + fileName);
			// newFile.createNewFile();
			out = new FileOutputStream(newFile);
			doc.write(out);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			out.close();
		}
	}

//	public static void main(String[] args) {
//		String filePath = "/Users/sudhamshbachu/eclipse-workspace-new/BupoAPI2/src/main/resources/reitTemplates/Contract_Amendment.doc";
//		POIFSFileSystem fs = null;
//		try {
//			fs = new POIFSFileSystem(new FileInputStream(filePath));
//			HWPFDocument doc = new HWPFDocument(fs);
//			doc = replaceText(doc, "{SELLER}", "Sudhamsh");
//			saveWord(filePath, doc);
//		} catch (FileNotFoundException e) {
//			e.printStackTrace();
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//	}

	private static HWPFDocument replaceText(HWPFDocument doc, String findText, String replaceText) {
		Range r1 = doc.getRange();

		for (int i = 0; i < r1.numSections(); ++i) {
			Section s = r1.getSection(i);
			for (int x = 0; x < s.numParagraphs(); x++) {
				Paragraph p = s.getParagraph(x);
				for (int z = 0; z < p.numCharacterRuns(); z++) {
					CharacterRun run = p.getCharacterRun(z);
					String text = run.text();
					if (text.contains(findText)) {
						run.replaceText(findText, replaceText);
					}
				}
			}
		}
		return doc;
	}

//	public static void main(String[] args) throws Exception {
//		int count = 0;
//		XWPFDocument document = new XWPFDocument();
//		ClassLoader classloader = Thread.currentThread().getContextClassLoader();
//		InputStream inputStream = classloader.getResourceAsStream("reitTemplates/Contract_Amendment.docx");
//		XWPFDocument docx = new XWPFDocument(inputStream);
//		XWPFWordExtractor we = new XWPFWordExtractor(docx);
//		String text = we.getText();
//		if (text.contains("FIRST")) {
//			text = text.replace("FIRST", "SECOND");
//			System.out.println(text);
//		}
//		char[] c = text.toCharArray();
//		for (int i = 0; i < c.length; i++) {
//
//			if (c[i] == '\n') {
//				count++;
//			}
//		}
//		System.out.println(c[0]);
//		StringTokenizer st = new StringTokenizer(text, "\n");
//
//		XWPFParagraph para = document.createParagraph();
//		para.setAlignment(ParagraphAlignment.CENTER);
//		XWPFRun run = para.createRun();
//		run.setBold(true);
//		run.setFontSize(36);
//		run.setText("Apache POI works well!");
//
//		List<XWPFParagraph> paragraphs = new ArrayList<XWPFParagraph>();
//		List<XWPFRun> runs = new ArrayList<XWPFRun>();
//		int k = 0;
//		for (k = 0; k < count + 1; k++) {
//			paragraphs.add(document.createParagraph());
//		}
//		k = 0;
//		while (st.hasMoreElements()) {
//			paragraphs.get(k).setAlignment(ParagraphAlignment.LEFT);
//			paragraphs.get(k).setSpacingAfter(0);
//			paragraphs.get(k).setSpacingBefore(0);
//			run = paragraphs.get(k).createRun();
//			run.setText(st.nextElement().toString());
//			k++;
//		}
//
//		File file = new File(classloader.getResource("out/test2.docx").toURI());
//		document.write(new FileOutputStream(file));
//	}

}
