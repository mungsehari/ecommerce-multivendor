package com.hari.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.MailException;
import org.springframework.mail.MailSendException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailService {
    private final JavaMailSender javaMailSender;

    public void sendVerificationOtpEmail(String userEmail,String otp,String subject,String text) throws MessagingException {
        try {

            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(
                    message,"utf-8"
            );

            helper.setSubject(subject);
            helper.setText(text);
            helper.setTo(userEmail);
            javaMailSender.send(message);
        }catch (MailException e){
            throw new MailSendException("failed to send email");
        }
    }
}
