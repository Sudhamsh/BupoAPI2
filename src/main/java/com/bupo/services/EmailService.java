package com.bupo.services;

import java.util.Map;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import com.bupo.util.CommonUtils;
import com.google.common.base.Preconditions;

public class EmailService {

	public void sendMail(String toAddress, String templateFileName, Map<String, String> templateValues) {

		Preconditions.checkNotNull(toAddress, "To address can't be null");
		// Recipient's email ID needs to be mentioned.

		// Sender's email ID needs to be mentioned
		final String from = "onesearchbuy@gmail.com";

		// Assuming you are sending email from through gmails smtp
		String host = "smtp.gmail.com";

		// Get system properties
		Properties properties = System.getProperties();

		// Setup mail server
		properties.put("mail.smtp.host", host);
		properties.put("mail.smtp.port", "465");
		properties.put("mail.smtp.ssl.enable", "true");
		properties.put("mail.smtp.auth", "true");

		// Get the Session object.// and pass username and password
		Session session = Session.getInstance(properties, new javax.mail.Authenticator() {

			protected PasswordAuthentication getPasswordAuthentication() {

				return new PasswordAuthentication(from, "tgukjxjofxqdvsqy");

			}

		});

		// Used to debug SMTP issues
		session.setDebug(true);

		try {
			// Create a default MimeMessage object.
			MimeMessage message = new MimeMessage(session);

			// Set From: header field of the header.
			message.setFrom(new InternetAddress(from));

			// Set To: header field of the header.
			message.addRecipient(Message.RecipientType.TO, new InternetAddress(toAddress));

			// Set Subject: header field
			message.setSubject("This is the Subject Line!");

			// Now email Template
			String emailTemplate = CommonUtils.readAllBytes(templateFileName);
			String templateDelimiter = "&&&";

			// replace template values
			for (Map.Entry<String, String> entry : templateValues.entrySet()) {
				String searchStr = templateDelimiter + entry.getKey() + templateDelimiter;
				System.out.println("searchStr --- " + searchStr);
				emailTemplate = emailTemplate.replaceAll(searchStr, entry.getValue());

			}
			message.setContent(emailTemplate, "text/html");

			System.out.println("sending...");
			// Send message
			Transport.send(message);
			System.out.println("Sent message successfully....");
		} catch (MessagingException mex) {
			mex.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
