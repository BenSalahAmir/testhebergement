package com.bezkoder.spring.security.mongodb.controllers;

import com.bezkoder.spring.security.mongodb.models.Notification;
import com.bezkoder.spring.security.mongodb.security.services.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/notifications")
public class NotificationController {

    @Autowired
    private NotificationService notificationService;

    @PostMapping
    public Notification createNotification(@RequestParam String message, @RequestParam String recipient) {
        return notificationService.createNotification(message, recipient);
    }

    @GetMapping("/{recipient}")
    public List<Notification> getNotificationsForRecipient(@PathVariable String recipient) {
        return notificationService.getNotificationsForRecipient(recipient);
    }

    @PutMapping("/{id}/read")
    public Notification markAsRead(@PathVariable String id) {
        return notificationService.markAsRead(id);
    }

    @DeleteMapping("/{id}")
    public void deleteNotification(@PathVariable String id) {
        notificationService.deleteNotification(id);
    }
}