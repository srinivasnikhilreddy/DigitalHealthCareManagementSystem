package com.iss.services;

import com.iss.models.Notification;
import com.iss.repositories.NotificationRepository;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class NotificationService
{
    private final NotificationRepository repository;

    public void sendNotification(Long userId, String type, String message)
    {
        Notification notification = Notification.builder()
                .userId(userId)
                .type(type)
                .message(message)
                .sent(true) // simulate sending
                .build();
        repository.save(notification);
    }
}
