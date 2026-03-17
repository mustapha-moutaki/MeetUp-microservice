package com.conferencehub.keynote.kafka;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class KeynoteEventProducer {

    private final KafkaTemplate<String, KeynoteEvent> kafkaTemplate;

    public KeynoteEventProducer(KafkaTemplate<String, KeynoteEvent> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendKeynoteCreatedEvent(KeynoteEvent event) {
        kafkaTemplate.send("keynote-events", event);
    }
}
