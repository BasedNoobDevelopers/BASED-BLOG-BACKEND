package com.noobsmoke.basedblogbackend.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.AllArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class EmailVerificationService {

    private final JavaMailSender mailSender;

    public void sendVerificationEmail(String to, String subject, String text) {
        try {
            MimeMessage emailMessage = mailSender.createMimeMessage();
            MimeMessageHelper emailMessageHelper = new MimeMessageHelper(emailMessage, true);

            emailMessageHelper.setTo(to);
            emailMessageHelper.setSubject(subject);
            emailMessageHelper.setText(text, true);

            mailSender.send(emailMessage);
        } catch (MessagingException messagingException) {
            throw new RuntimeException("Email Not Sent " + messagingException.getMessage());
        }

    }
}
