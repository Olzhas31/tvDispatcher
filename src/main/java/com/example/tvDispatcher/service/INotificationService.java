package com.example.tvDispatcher.service;

import com.example.tvDispatcher.entity.Notification;
import com.example.tvDispatcher.entity.User;

import java.util.List;

public interface INotificationService {
    List<Notification> getNotificationsByUserAndLimit(User user, Integer limit);

    void userReadNotifications(User user);

    List<Notification> getAllByUser(User user);

    void create(String content);
}
