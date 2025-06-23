package com.kulift.lift.global.service;

import org.springframework.core.env.Environment;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import com.kulift.lift.global.exception.CustomException;
import com.kulift.lift.global.exception.ErrorCode;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class EmailService {

	private final JavaMailSender mailSender;
	private final Environment env;
	private final SpringTemplateEngine templateEngine;

	public void sendVerificationEmail(String to, String token) throws MessagingException {
		try {
			String subject = "비밀번호 재설정 안내";
			String resetLink = "http://kulift.com/reset-password?token=" + token;

			Context context = new Context();
			context.setVariable("resetLink", resetLink);
			String message = templateEngine.process("email/verification", context);

			MimeMessage mimeMessage = mailSender.createMimeMessage();
			MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "utf-8");

			helper.setTo(to);
			helper.setSubject(subject);
			helper.setText(message, true);

			mailSender.send(mimeMessage);
		} catch (MessagingException e) {
			throw new CustomException(ErrorCode.EMAIL_SEND_FAILED);
		}
	}
}
