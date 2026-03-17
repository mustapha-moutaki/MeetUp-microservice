package com.conferencehub.notification.kafka;

import com.conferencehub.notification.entity.Notification;
import com.conferencehub.notification.enums.TypeEvenement;
import com.conferencehub.notification.service.NotificationService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class EventsConsumer {

    private final NotificationService service;
    private final ObjectMapper objectMapper;

    // Simulate getting managers email - in real life this could be fetched from DB
    // or config
    private final String MANAGERS_EMAIL = "managers@conferencehub.com";

    public EventsConsumer(NotificationService service, ObjectMapper objectMapper) {
        this.service = service;
        this.objectMapper = objectMapper;
    }

    @KafkaListener(topics = "keynote-events", groupId = "notification-group")
    public void handleKeynoteEvent(String message) {
        try {
            JsonNode node = objectMapper.readTree(message);
            if ("KEYNOTE_CREATED".equals(node.get("type").asText())) {
                String email = node.get("email").asText();
                String prenom = node.get("prenom").asText();
                Long keynoteId = node.get("keynoteId").asLong();

                creerNotification(email, "Bienvenue " + prenom + " !",
                        "Votre profil intervenant a bien été créé.",
                        TypeEvenement.KEYNOTE_CREATED, keynoteId);
            }
        } catch (Exception e) {
            System.err.println("Erreur Kafka keynote-events: " + e.getMessage());
        }
    }

    @KafkaListener(topics = "conference-events", groupId = "notification-group")
    public void handleConferenceEvent(String message) {
        try {
            JsonNode node = objectMapper.readTree(message);
            String type = node.get("type").asText();
            Long conferenceId = node.get("conferenceId").asLong();
            String titre = node.get("titre").asText();

            if ("CONFERENCE_CREATED".equals(type)) {
                creerNotification(MANAGERS_EMAIL, "Nouvelle Conférence Créée",
                        "La conférence '" + titre + "' vient d'être créée.",
                        TypeEvenement.CONFERENCE_CREATED, conferenceId);

            } else if ("STATUS_CHANGED".equals(type)) {
                String statut = node.get("payloadDetails").asText();
                if ("EN_COURS".equals(statut)) {
                    // Normalement, fetcher la liste des inscrits, ici simplifié par un email
                    // générique aux inscrits
                    creerNotification("inscrits_" + conferenceId + "@conferencehub.com",
                            "Rappel : La conférence commence !",
                            "La conférence '" + titre + "' vient de passer en cours.",
                            TypeEvenement.STATUS_CHANGED, conferenceId);
                } else if ("ANNULEE".equals(statut)) {
                    creerNotification("inscrits_" + conferenceId + "@conferencehub.com",
                            "Urgences : Conférence Annulée",
                            "Désolé, la conférence '" + titre + "' a été annulée.",
                            TypeEvenement.STATUS_CHANGED, conferenceId);
                }
            }
        } catch (Exception e) {
            System.err.println("Erreur Kafka conference-events: " + e.getMessage());
        }
    }

    private void creerNotification(String email, String sujet, String contenu, TypeEvenement type, Long refId) {
        Notification notification = Notification.builder()
                .destinataire(email)
                .sujet(sujet)
                .contenu(contenu)
                .typeEvenement(type)
                .referenceId(refId)
                .build();
        service.processAndSave(notification);
    }
}
