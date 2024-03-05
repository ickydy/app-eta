package org.edupoll.app.service;

import org.edupoll.app.entity.Account;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class MailService {

	private final JavaMailSender javaMailSender;
	private final TemplateEngine templateEngine;

	public void sendAuthenticationCode(String email, int authenticationCode) {
		MimeMessage message = javaMailSender.createMimeMessage();

		try {
			MimeMessageHelper helper = new MimeMessageHelper(message, false, "UTF-8");

			helper.setTo(email);
			helper.setFrom("no-reply@gmail.com");
			helper.setSubject("[ANYTIME] 인증코드 발급완료.");

			Context ctx = new Context();
			ctx.setVariable("authenticationCode", authenticationCode);
			String text = templateEngine.process("mail/authentication-code", ctx);
			helper.setText(text, true);

			javaMailSender.send(helper.getMimeMessage());
		} catch (Exception e) {
			log.warn("Fail to send mail. Cause : " + e.getMessage());
		}
	}

	public void sendForgotId(String email, Account account) {
		MimeMessage message = javaMailSender.createMimeMessage();
		
		try {
			MimeMessageHelper helper = new MimeMessageHelper(message, false, "UTF-8");

			helper.setTo(email);
			helper.setFrom("no-reply@gmail.com");
			helper.setSubject("[ANYTIME] 아이디 찾기");

			Context ctx = new Context();
			ctx.setVariable("account", account);
			String text = templateEngine.process("mail/forgot-id", ctx);
			helper.setText(text, true);

			javaMailSender.send(helper.getMimeMessage());
		} catch (Exception e) {
			log.warn("Fail to send mail. Cause : " + e.getMessage());
		}
	}
	
	public void sendTempPassword(String email, String tempPassword) {
		MimeMessage message = javaMailSender.createMimeMessage();
		
		try {
			MimeMessageHelper helper = new MimeMessageHelper(message, false, "UTF-8");

			helper.setTo(email);
			helper.setFrom("no-reply@gmail.com");
			helper.setSubject("[ANYTIME] 임시 비밀번호 발급");

			Context ctx = new Context();
			ctx.setVariable("tempPassword", tempPassword);
			String text = templateEngine.process("mail/forgot-password", ctx);
			helper.setText(text, true);

			javaMailSender.send(helper.getMimeMessage());
		} catch (Exception e) {
			log.warn("Fail to send mail. Cause : " + e.getMessage());
		}
	}
}
