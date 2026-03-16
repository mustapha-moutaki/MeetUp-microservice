package com.conferencehub.conference.service.impl;

import com.conferencehub.conference.dto.ConferenceRequestDTO;
import com.conferencehub.conference.dto.ConferenceResponseDTO;
import com.conferencehub.conference.entity.Conference;
import com.conferencehub.conference.enums.ConferenceStatut;
import com.conferencehub.conference.enums.ConferenceType;
import com.conferencehub.conference.exception.ConferenceNotFoundException;
import com.conferencehub.conference.feign.KeynoteFeignClient;
import com.conferencehub.conference.kafka.KafkaEventProducer;
import com.conferencehub.conference.mapper.ConferenceMapper;
import com.conferencehub.conference.repository.ConferenceRepository;
import com.conferencehub.conference.service.ConferenceService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.stream.Collectors;

@Service
@Transactional
public class ConferenceServiceImpl implements ConferenceService {

    private final ConferenceRepository repository;
    private final ConferenceMapper mapper;
    private final KeynoteFeignClient keynoteClient;
    private final KafkaEventProducer eventProducer;

    public ConferenceServiceImpl(ConferenceRepository repository,
            ConferenceMapper mapper,
            KeynoteFeignClient keynoteClient,
            KafkaEventProducer eventProducer) {
        this.repository = repository;
        this.mapper = mapper;
        this.keynoteClient = keynoteClient;
        this.eventProducer = eventProducer;
    }

    @Override
    public ConferenceResponseDTO createConference(ConferenceRequestDTO dto) {
        // Validate that all keynotes exist in the Keynote Service before saving
        if (dto.getKeynoteIds() != null && !dto.getKeynoteIds().isEmpty()) {
            for (Long keynoteId : dto.getKeynoteIds()) {
                com.conferencehub.conference.dto.KeynoteDTO keynote = keynoteClient.getKeynoteById(keynoteId);
                // The fallback returns "Indisponible" if the service is down or if the ID is
                // not found (404)
                if (keynote == null || "Indisponible".equals(keynote.getNom())) {
                    throw new IllegalArgumentException("Impossible de créer la conférence : le keynote de l'ID "
                            + keynoteId + " n'existe pas ou le service est indisponible.");
                }
            }
        }

        Conference conference = mapper.toEntity(dto);
        Conference saved = repository.save(conference);
        eventProducer.sendConferenceCreatedEvent(saved);
        return mapper.toDTO(saved);
    }

    @Override
    public ConferenceResponseDTO updateConference(Long id, ConferenceRequestDTO dto) {
        Conference conference = repository.findById(id)
                .orElseThrow(() -> new ConferenceNotFoundException("Conference not found with id: " + id));
        mapper.updateEntityFromDto(dto, conference);
        Conference updated = repository.save(conference);
        return mapper.toDTO(updated);
    }

    @Override
    public void deleteConference(Long id) {
        repository.deleteById(id);
    }

    @Override
    public ConferenceResponseDTO getConferenceById(Long id) {
        Conference conference = repository.findById(id)
                .orElseThrow(() -> new ConferenceNotFoundException("Conference not found with id: " + id));

        ConferenceResponseDTO responseDTO = mapper.toDTO(conference);
        if (conference.getKeynoteIds() != null && !conference.getKeynoteIds().isEmpty()) {
            responseDTO.setKeynotes(conference.getKeynoteIds().stream()
                    .map(keynoteClient::getKeynoteById)
                    .collect(Collectors.toList()));
        }
        return responseDTO;
    }

    @Override
    public Page<ConferenceResponseDTO> getAllConferences(String titre, ConferenceType type, Pageable pageable) {
        return repository.findByTitreContainingIgnoreCaseOrType(
                titre != null ? titre : "", type, pageable)
                .map(mapper::toDTO);
    }

    @Override
    public ConferenceResponseDTO updateStatut(Long id, ConferenceStatut statut) {
        Conference conference = repository.findById(id)
                .orElseThrow(() -> new ConferenceNotFoundException("Conference not found with id: " + id));
        conference.setStatut(statut);
        Conference updated = repository.save(conference);
        eventProducer.sendConferenceStatusChangedEvent(updated);
        return mapper.toDTO(updated);
    }

    @Override
    public boolean existsByKeynoteId(Long keynoteId) {
        return repository.existsByKeynoteId(keynoteId);
    }
}
