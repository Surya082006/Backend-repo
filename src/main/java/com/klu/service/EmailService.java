package com.klu.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired(required = false) // 🔥 IMPORTANT FIX
    private JavaMailSender mailSender;

    public void sendEmail(String to, String subject, String text) {

        // 🔥 Check if mailSender exists
        if (mailSender == null) {
            System.out.println("Email service not configured. Skipping email...");
            return;
        }

        SimpleMailMessage msg = new SimpleMailMessage();
        msg.setTo(to);
        msg.setSubject(subject);
        msg.setText(text);

        mailSender.send(msg);
    }
}