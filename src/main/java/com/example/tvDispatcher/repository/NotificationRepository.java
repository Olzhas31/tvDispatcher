package com.example.tvDispatcher.repository;

import com.example.tvDispatcher.entity.Notification;
import com.example.tvDispatcher.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {

    List<Notification> findAllByUserAndIsRead(User user, Boolean isRead);

    List<Notification> findAllByUser(User user);
}