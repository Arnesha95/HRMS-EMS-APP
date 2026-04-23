//package com.vvdn.ems_backend.services.impl;
//
//import lombok.RequiredArgsConstructor;
//import org.springframework.mail.javamail.JavaMailSender;
//import org.springframework.stereotype.Service;
//
//@Service
//@RequiredArgsConstructor
//public class EmailService {
//
//    private final JavaMailSender mailSender;
//
//    public void sendResetEmail(String to, String token) {
//
//        String resetLink = "http://localhost:3000/reset-password?token=" + token;
//
//
//        // Replace with real email logic (SMTP / SendGrid)
//        System.out.println("Send email to: " + to);
//        System.out.println("Reset link: " + resetLink);
//    }
//}
