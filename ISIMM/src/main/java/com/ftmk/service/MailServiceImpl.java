package com.ftmk.service;

import javax.mail.Message;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessagePreparator;

public class MailServiceImpl implements MailService {

	@Autowired
	JavaMailSender mailSender;

	@Override
	public void sendEmail(String senderEmailId, String receiverEmailId, String subject, String message) {

		MimeMessagePreparator preparator = new MimeMessagePreparator() {

			@Override
			public void prepare(MimeMessage mimeMessage) throws Exception {

				mimeMessage.setFrom(senderEmailId);
				mimeMessage.setRecipient(Message.RecipientType.TO, new InternetAddress(receiverEmailId));
				mimeMessage.setSubject(subject);
				mimeMessage.setText(message);

			}
		};
		try {

			mailSender.send(preparator);
			System.out.println("Message sent...");

		} catch (Exception e) {

			e.printStackTrace();
		}

	}

}
