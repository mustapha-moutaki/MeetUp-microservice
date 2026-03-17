package com.conferencehub.notification.service.impl;

import com.conferencehub.notification.entity.Notification;
import com.conferencehub.notification.enums.NotificationStatut;
import com.conferencehub.notification.repository.NotificationRepository;
import com.conferencehub.notification.service.NotificationService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class NotificationServiceImpl implements NotificationService {

    private final NotificationRepository repository;

    public NotificationServiceImpl(NotificationRepository repository) {
        this.repository = repository;
    }

    @Override
    public void processAndSave(Notification notification) {
        notification.setTentatives(notification.getTentatives() + 1);

        try {
            // Logique d'envoi d'email (Simulé)
            System.out.println(
                    "Envoi de l'email à : " + notification.getDestinataire() + " -> " + notification.getSujet());

            // Simulation d'une erreur aléatoire pour tester le Retry
            if (Math.random() > 0.9) {
                throw new RuntimeException("Simulated Mail Server Timeout");
            }

            notification.setStatut(NotificationStatut.ENVOYEE);
            notification.setDateEnvoi(LocalDateTime.now());
            notification.setErreurMessage(null);

        } catch (Exception e) {
            notification.setStatut(NotificationStatut.ECHOUEE);
            notification.setErreurMessage(e.getMessage());
        }

        repository.save(notification);
    }

    @Override
    public Page<Notification> getAllNotifications(Pageable pageable) {
        return repository.findAll(pageable);
    }

    @Override
    public List<Notification> getNotificationsByEmail(String email) {
        return repository.findByDestinataire(email);
    }

    @Override
    public List<Notification> getNotificationsByConferenceId(Long conferenceId) {
        return repository.findByReferenceId(conferenceId);
    }

    @Override
    public List<Notification> getFailedNotifications() {
        return repository.findByStatut(NotificationStatut.ECHOUEE);
    }

    @Override
    public void retryNotification(Long id) {
        Notification notification = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Notification introuvable"));

        if (notification.getStatut() == NotificationStatut.ECHOUEE) {
            System.out.println("Retrying notification " + id);
            processAndSave(notification);
        } else {
            throw new IllegalStateException("Cette notification n'est pas en échec.");
        }
    }
}
