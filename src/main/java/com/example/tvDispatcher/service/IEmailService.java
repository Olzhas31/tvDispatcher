package com.example.tvDispatcher.service;

public interface IEmailService {
    void sendMessage(String email, String newPassword);

    void sendMessage(String email, String title, String content);
}
