package com.conferencehub.conference.kafka;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ConferenceEvent {
    private String type; // e.g., CONFERENCE_CREATED, STATUS_CHANGED, REVIEW_SUBMITTED,
                         // PARTICIPANT_REGISTERED
    private Long conferenceId;
    private String titre;
    private String payloadDetails; // Pour les emails ou infos supplémentaires
}
