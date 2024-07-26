package com.dev.computer_accessories.auth.service.impl;

import com.dev.computer_accessories.auth.service.MailMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MailMessageImpl implements MailMessage {
    private final JavaMailSender mailSender;

    @Override
    public void sendMessage(String to, String subject, String body) {
        SimpleMailMessage mailMessage =  new SimpleMailMessage();

        mailMessage.setFrom("hoangduyphuong30102002@gmail.com");
        mailMessage.setTo(to);
        mailMessage.setSubject(subject);
        mailMessage.setText(body);

        mailSender.send(mailMessage);
    }
}
