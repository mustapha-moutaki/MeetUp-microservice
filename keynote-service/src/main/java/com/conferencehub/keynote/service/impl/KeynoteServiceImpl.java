package com.conferencehub.keynote.service.impl;

import com.conferencehub.keynote.dto.KeynoteRequestDTO;
import com.conferencehub.keynote.dto.KeynoteResponseDTO;
import com.conferencehub.keynote.entity.Keynote;
import com.conferencehub.keynote.exception.KeynoteNotFoundException;
import com.conferencehub.keynote.kafka.KeynoteEvent;
import com.conferencehub.keynote.kafka.KeynoteEventProducer;
import com.conferencehub.keynote.feign.ConferenceFeignClient;
import com.conferencehub.keynote.mapper.KeynoteMapper;
import com.conferencehub.keynote.repository.KeynoteRepository;
import com.conferencehub.keynote.service.KeynoteService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class KeynoteServiceImpl implements KeynoteService {

    private final KeynoteRepository repository;
    private final KeynoteMapper mapper;
    private final KeynoteEventProducer eventProducer;
    private final ConferenceFeignClient conferenceClient;

    public KeynoteServiceImpl(KeynoteRepository repository, KeynoteMapper mapper, KeynoteEventProducer eventProducer,
            ConferenceFeignClient conferenceClient) {
        this.repository = repository;
        this.mapper = mapper;
        this.eventProducer = eventProducer;
        this.conferenceClient = conferenceClient;
    }

    @Override
    public KeynoteResponseDTO createKeynote(KeynoteRequestDTO dto) {
        if (repository.existsByEmail(dto.getEmail())) {
            throw new IllegalArgumentException("L'email est déjà utilisé par un autre keynote.");
        }

        Keynote keynote = mapper.toEntity(dto);
        Keynote saved = repository.save(keynote);

        KeynoteEvent event = KeynoteEvent.builder()
                .type("KEYNOTE_CREATED")
                .keynoteId(saved.getId())
                .nom(saved.getNom())
                .prenom(saved.getPrenom())
                .email(saved.getEmail())
                .build();
        eventProducer.sendKeynoteCreatedEvent(event);

        return mapper.toDTO(saved);
    }

    @Override
    public KeynoteResponseDTO updateKeynote(Long id, KeynoteRequestDTO dto) {
        Keynote keynote = repository.findById(id)
                .orElseThrow(() -> new KeynoteNotFoundException("Keynote introuvable avec l'id : " + id));

        if (!keynote.getEmail().equals(dto.getEmail()) && repository.existsByEmail(dto.getEmail())) {
            throw new IllegalArgumentException("L'email est déjà utilisé par un autre keynote.");
        }

        mapper.updateEntityFromDto(dto, keynote);
        Keynote updated = repository.save(keynote);

        return mapper.toDTO(updated);
    }

    @Override
    public void deleteKeynote(Long id) {
        Keynote keynote = repository.findById(id)
                .orElseThrow(() -> new KeynoteNotFoundException("Keynote introuvable avec l'id : " + id));

        try {
            Boolean isLinked = conferenceClient.existsByKeynoteId(id);
            if (Boolean.TRUE.equals(isLinked)) {
                throw new com.conferencehub.keynote.exception.KeynoteLinkedToConferenceException(
                        "Ce keynote est lié à une conférence et ne peut pas être supprimé.");
            }
        } catch (Exception e) {
            // Si le call Feign échoue (service indisponible ou autre) -> ou qu'il throw
            // Linked Exception
            if (e instanceof com.conferencehub.keynote.exception.KeynoteLinkedToConferenceException) {
                throw e;
            }
            // Assume we can't delete if we can't verify (or we could fallback to allow, but
            // strict is safer)
            throw new IllegalStateException(
                    "Impossible de vérifier si le keynote est lié à une conférence : " + e.getMessage());
        }

        repository.delete(keynote);
    }

    @Override
    public KeynoteResponseDTO getKeynoteById(Long id) {
        return repository.findById(id)
                .map(mapper::toDTO)
                .orElseThrow(() -> new KeynoteNotFoundException("Keynote introuvable avec l'id : " + id));
    }

    @Override
    public Page<KeynoteResponseDTO> getAllKeynotes(String keyword, Pageable pageable) {
        Page<Keynote> page;
        if (keyword != null && !keyword.isEmpty()) {
            page = repository.findByNomContainingOrPrenomContainingOrFonctionContaining(keyword, keyword, keyword,
                    pageable);
        } else {
            page = repository.findAll(pageable);
        }
        return page.map(mapper::toDTO);
    }
}
