package com.bezkoder.spring.security.mongodb.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;

import com.bezkoder.spring.security.mongodb.models.Notification;
import com.bezkoder.spring.security.mongodb.repository.NotificationRepository;

import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api")
public class NotificationControllerWebSc {

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @Autowired
    private NotificationRepository notificationRepository;

    @PostMapping("/notify/{username}")
    public void notifyUser(@PathVariable String username, @RequestBody NotificationMessage message) {
        // Save notification to the database
        Notification notification = new Notification();
        notification.setUsername(username);
        notification.setContent(message.getContent());
        notification.setTimestamp(System.currentTimeMillis());
        notification.setIsRead(false);
        notificationRepository.save(notification);

        // Send notification to the user
        messagingTemplate.convertAndSendToUser(username, "/queue/notifications", message.getContent());
    }

    @GetMapping("/notifications/{username}")
    public List<Notification> getUserNotifications(@PathVariable String username) {
        return notificationRepository.findByUsername(username);
    }

    static class NotificationMessage {
        private String content;

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }
    }
}
