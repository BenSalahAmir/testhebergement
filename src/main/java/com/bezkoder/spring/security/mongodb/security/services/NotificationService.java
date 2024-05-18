package com.bezkoder.spring.security.mongodb.security.services;


import com.bezkoder.spring.security.mongodb.models.Notification;

import com.bezkoder.spring.security.mongodb.repository.NotificationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NotificationService {

    @Autowired
    private NotificationRepository notificationRepository;

    public Notification createNotification(String message, String recipient) {
        Notification notification = new Notification(message, recipient);
        return notificationRepository.save(notification);
    }

    public List<Notification> getNotificationsForRecipient(String recipient) {
        return notificationRepository.findByRecipient(recipient);
    }

    public Notification markAsRead(String id) {
        Notification notification = notificationRepository.findById(id).orElseThrow(() -> new RuntimeException("Notification not found"));
        notification.setRead(true);
        return notificationRepository.save(notification);
    }

    public void deleteNotification(String id) {
        notificationRepository.deleteById(id);
    }
}
