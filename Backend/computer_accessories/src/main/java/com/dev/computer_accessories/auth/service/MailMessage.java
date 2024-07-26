package com.dev.computer_accessories.auth.service;

public interface MailMessage {
    void sendMessage(String to, String subject, String body);
}
