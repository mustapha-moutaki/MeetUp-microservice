package com.conferencehub.notification.service;

import com.conferencehub.notification.entity.Notification;
import com.conferencehub.notification.enums.NotificationStatut;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface NotificationService {
    void processAndSave(Notification notification);

    Page<Notification> getAllNotifications(Pageable pageable);

    List<Notification> getNotificationsByEmail(String email);

    List<Notification> getNotificationsByConferenceId(Long conferenceId);

    List<Notification> getFailedNotifications();

    void retryNotification(Long id);
}
