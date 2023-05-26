package com.example.tvDispatcher.service.impl;

import com.example.tvDispatcher.service.IEmailService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.AllArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class EmailServiceImpl implements IEmailService {

    private final JavaMailSender emailSender;

    @Override
    public void sendMessage(String email, String newPassword) {
        MimeMessage message = emailSender.createMimeMessage();
        MimeMessageHelper helper = null;
        try {
            helper = new MimeMessageHelper(message, true, "utf-8");
            message.setContent("Ақпаттық жүйедегі құпия сөз өзгерді. Егер сіз өзгертпеген болсаңыз бұл хабарламаны елемесеңіз болады! <br>" +
                    "Жаңа құпия сөз: " + newPassword, "text/html");
            helper.setTo(email);
            helper.setSubject("Құпия сөз өзгерді");
        } catch (MessagingException e) {
            e.printStackTrace();
        }

        emailSender.send(message);
    }

    @Override
    public void sendMessage(String email, String title, String content) {
        MimeMessage message = emailSender.createMimeMessage();
        MimeMessageHelper helper = null;
        try {
            helper = new MimeMessageHelper(message, true, "utf-8");
            message.setContent(content, "text/html");
            helper.setTo(email);
            helper.setSubject(title);
        } catch (MessagingException e) {
            e.printStackTrace();
        }

        emailSender.send(message);
    }
}
