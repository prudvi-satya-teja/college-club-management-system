package com.project.aclub.service;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {
    private final JavaMailSender mailSender;

    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void sendOtpEmail(String toEmail, String otp) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(toEmail);
        message.setSubject("AClub - Password Reset OTP");
        message.setText("Your OTP for password reset is: " + otp
                + "\n\nThis OTP is valid for 10 minutes.\n\n" +
                "If you did not request this, please ignore.");
        mailSender.send(message);
    }
}