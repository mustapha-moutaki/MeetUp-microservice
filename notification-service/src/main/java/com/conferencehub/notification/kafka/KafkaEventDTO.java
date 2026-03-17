package com.conferencehub.notification.kafka;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class KafkaEventDTO {
    private String type;
    private Long referenceId; // keynoteId ou conferenceId selon l'événement
    private String titre;
    private String payloadDetails; // email, prenom, statut
}
