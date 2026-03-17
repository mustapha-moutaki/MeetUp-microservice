package com.conferencehub.notification.repository;

import com.conferencehub.notification.entity.Notification;
import com.conferencehub.notification.enums.NotificationStatut;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {
    List<Notification> findByDestinataire(String email);

    List<Notification> findByReferenceId(Long referenceId);

    List<Notification> findByStatut(NotificationStatut statut);

    Page<Notification> findAll(Pageable pageable);
}
