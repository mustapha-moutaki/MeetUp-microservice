package com.conferencehub.conference.service;

import com.conferencehub.conference.dto.ConferenceRequestDTO;
import com.conferencehub.conference.dto.ConferenceResponseDTO;
import com.conferencehub.conference.enums.ConferenceStatut;
import com.conferencehub.conference.enums.ConferenceType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ConferenceService {
    ConferenceResponseDTO createConference(ConferenceRequestDTO dto);

    ConferenceResponseDTO updateConference(Long id, ConferenceRequestDTO dto);

    void deleteConference(Long id);

    ConferenceResponseDTO getConferenceById(Long id);

    Page<ConferenceResponseDTO> getAllConferences(String titre, ConferenceType type, Pageable pageable);

    ConferenceResponseDTO updateStatut(Long id, ConferenceStatut statut);

    boolean existsByKeynoteId(Long keynoteId);
}
