package com.fashionapp.util;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

@Component("mailsender")
public class MailSender {

	@Autowired
	private JavaMailSender sender;

	public void sendmail(String mail, String text) {
		MimeMessage msg = sender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(msg);
		try {
			helper.setTo(mail);
			helper.setSubject("FashionApp login ");
			helper.setText(text);
		} catch (MessagingException e) {
			e.printStackTrace();
		}
		sender.send(msg);

	}

}
