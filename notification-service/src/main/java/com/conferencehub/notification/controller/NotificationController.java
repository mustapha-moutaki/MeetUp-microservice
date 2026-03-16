package com.conferencehub.notification.controller;

import com.conferencehub.notification.entity.Notification;
import com.conferencehub.notification.service.NotificationService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/notifications")
public class NotificationController {

    private final NotificationService service;

    public NotificationController(NotificationService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<Page<Notification>> getAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(service.getAllNotifications(PageRequest.of(page, size)));
    }

    @GetMapping("/search")
    public ResponseEntity<List<Notification>> search(
            @RequestParam(required = false) String email,
            @RequestParam(required = false) Long conferenceId,
            @RequestParam(required = false, defaultValue = "false") boolean failedOnly) {

        if (failedOnly) {
            return ResponseEntity.ok(service.getFailedNotifications());
        }
        if (email != null) {
            return ResponseEntity.ok(service.getNotificationsByEmail(email));
        }
        if (conferenceId != null) {
            return ResponseEntity.ok(service.getNotificationsByConferenceId(conferenceId));
        }
        return ResponseEntity.badRequest().build();
    }

    @PostMapping("/retry/{id}")
    public ResponseEntity<String> retry(@PathVariable Long id) {
        try {
            service.retryNotification(id);
            return ResponseEntity.ok("Retry lancé pour la notification " + id);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
