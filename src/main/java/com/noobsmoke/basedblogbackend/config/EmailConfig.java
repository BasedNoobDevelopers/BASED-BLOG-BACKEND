package com.noobsmoke.basedblogbackend.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.util.Properties;

@Configuration
public class EmailConfig {

    @Value("${SUPPORT_EMAIL}")
    private String emailUsername;

    @Value("${APP_PASSWORD}")
    private String password;

    @Value("${spring.mail.host}")
    private String emailHost;

    @Value("${spring.mail.port}")
    private int emailPort;

    @Bean
    public JavaMailSender mailSender() {
        JavaMailSenderImpl mailSenderImp = new JavaMailSenderImpl();
        mailSenderImp.setHost(emailHost);
        mailSenderImp.setPort(emailPort);
        mailSenderImp.setUsername(emailUsername);
        mailSenderImp.setPassword(password);

        Properties properties = mailSenderImp.getJavaMailProperties();
        properties.put("mail.transport.protocol", "smtp");
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.debug", "true");

        return mailSenderImp;
    }

}
