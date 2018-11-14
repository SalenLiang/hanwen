package com.fahai.cc.service.mail;

import java.util.Date;

import javax.mail.Address;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
@Component
public class SendMail {
	@Autowired
	private JavaMailSenderImpl mailSender;
	
	public void setMailSender(JavaMailSenderImpl mailSender) {
		this.mailSender = mailSender;
	}
	public void sendEmail(EmailVo emailVo) throws MessagingException {
		mailSender.setHost(emailVo.getHost());
		mailSender.setUsername(emailVo.getSender());
		mailSender.setPassword(emailVo.getPassword());
		MimeMessage message = mailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(message,true);
		//发件人
		helper.setFrom(emailVo.getSender());
		//收件人
		Address[] addresses = new Address[ emailVo.getReceivers().length];
		for (int i = 0; i < addresses.length; i++) {
			addresses[i] = new InternetAddress( emailVo.getReceivers()[i]);
		}
		message.setRecipients(Message.RecipientType.TO, addresses);
		//邮件标题
		helper.setSubject(emailVo.getSubject());
		//邮件正文
		helper.setText(emailVo.getEmailContent(),true);
		//发送时间
		helper.setSentDate(new Date());
		//发送邮件
		mailSender.send(message);
	}


}
