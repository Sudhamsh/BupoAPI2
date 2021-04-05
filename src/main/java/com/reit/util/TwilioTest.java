package com.reit.util;

import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;

public class TwilioTest {
	// Find your Account Sid and Token at twilio.com/user/account
	public static final String ACCOUNT_SID = "ACbe5f1d8f1eeaa3829f536e66486b1d60";
	public static final String AUTH_TOKEN = "637426bd92968c86be6579f9f0d9dbea";

	public static void main(String[] args) {
		Twilio.init(ACCOUNT_SID, AUTH_TOKEN);

		Message message = Message.creator(new PhoneNumber("+14806867016"), new PhoneNumber("+17065842196"),
				"Hi Bhavya - SMS from Twilio. Love you").create();

		System.out.println(message.getSid());
	}
}
