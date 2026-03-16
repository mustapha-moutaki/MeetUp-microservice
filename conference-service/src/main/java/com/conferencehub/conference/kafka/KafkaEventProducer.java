package com.conferencehub.conference.kafka;

import com.conferencehub.conference.entity.Conference;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class KafkaEventProducer {

    private final KafkaTemplate<String, ConferenceEvent> kafkaTemplate;

    public KafkaEventProducer(KafkaTemplate<String, ConferenceEvent> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendConferenceCreatedEvent(Conference conference) {
        ConferenceEvent event = ConferenceEvent.builder()
                .type("CONFERENCE_CREATED")
                .conferenceId(conference.getId())
                .titre(conference.getTitre())
                .build();
        kafkaTemplate.send("conference-events", event);
    }

    public void sendConferenceStatusChangedEvent(Conference conference) {
        ConferenceEvent event = ConferenceEvent.builder()
                .type("STATUS_CHANGED")
                .conferenceId(conference.getId())
                .titre(conference.getTitre())
                .payloadDetails(conference.getStatut().name())
                .build();
        kafkaTemplate.send("conference-events", event);
    }
}
