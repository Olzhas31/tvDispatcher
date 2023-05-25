package com.example.tvDispatcher.service.impl;

import com.example.tvDispatcher.entity.Notification;
import com.example.tvDispatcher.entity.User;
import com.example.tvDispatcher.repository.NotificationRepository;
import com.example.tvDispatcher.service.INotificationService;
import lombok.AllArgsConstructor;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;

@Service
@AllArgsConstructor
public class NotificationServiceImpl implements INotificationService {

    private final NotificationRepository notificationRepository;

    @Override
    public List<Notification> getNotificationsByUserAndLimit(User user, Integer limit) {
        return notificationRepository.findAllByUserAndIsRead(user, false)
                .stream()
                .sorted((Comparator.comparing(Notification::getCreatedTime).reversed()))
                .limit(limit)
                .toList();
    }

    @Override
    public void userReadNotifications(User user) {
        List<Notification> notifications = notificationRepository.findAllByUserAndIsRead(user, false);
        for (Notification notification : notifications) {
            notification.setIsRead(true);
            notificationRepository.save(notification);
        }
    }

    @Override
    public List<Notification> getAllByUser(User user) {
        return notificationRepository.findAllByUser(user)
                .stream()
                .sorted((Comparator.comparing(Notification::getCreatedTime).reversed()))
                .toList();
    }

    @Override
    public void create(String content) {

    }


}
