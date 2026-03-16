package com.conferencehub.notification.entity;

import com.conferencehub.notification.enums.NotificationStatut;
import com.conferencehub.notification.enums.TypeEvenement;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "notifications")
public class Notification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Email
    private String destinataire;

    @NotBlank
    private String sujet;

    @NotBlank
    @Column(columnDefinition = "TEXT")
    private String contenu;

    @Enumerated(EnumType.STRING)
    private TypeEvenement typeEvenement;

    private Long referenceId;

    @Enumerated(EnumType.STRING)
    @Builder.Default
    private NotificationStatut statut = NotificationStatut.EN_ATTENTE;

    private LocalDateTime dateEnvoi;

    @Builder.Default
    private Integer tentatives = 0;

    private String erreurMessage;
}
