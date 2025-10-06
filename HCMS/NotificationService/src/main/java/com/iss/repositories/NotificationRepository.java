package com.iss.repositories;

import com.iss.models.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.*;

public interface NotificationRepository extends JpaRepository<Notification, Long>
{
    List<Notification> findByUserId(Long userId);
}
