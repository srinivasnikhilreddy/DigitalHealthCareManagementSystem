package com.iss.controllers;

import com.iss.models.Notification;
import com.iss.services.NotificationService;
import com.iss.repositories.NotificationRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/notifications")
@CrossOrigin(origins = "http://localhost:4200")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationRepository notificationRepository;
    private final NotificationService notificationService;

    @GetMapping("/user/{userId}")
    public List<Notification> getNotificationsByUser(@PathVariable Long userId)
    {
        return notificationRepository.findByUserId(userId);
    }

    @PutMapping("/{notificationId}/sent")
    public Notification markNotificationAsSent(@PathVariable Long notificationId)
    {
        Notification notification = notificationRepository.findById(notificationId)
                .orElseThrow(() -> new RuntimeException("Notification not found"));

        notification.setSent(true);
        return notificationRepository.save(notification);
    }

}
