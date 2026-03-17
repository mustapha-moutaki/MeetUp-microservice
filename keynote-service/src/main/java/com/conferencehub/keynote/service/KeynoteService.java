package com.conferencehub.keynote.service;

import com.conferencehub.keynote.dto.KeynoteRequestDTO;
import com.conferencehub.keynote.dto.KeynoteResponseDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface KeynoteService {
    KeynoteResponseDTO createKeynote(KeynoteRequestDTO dto);

    KeynoteResponseDTO updateKeynote(Long id, KeynoteRequestDTO dto);

    void deleteKeynote(Long id);

    KeynoteResponseDTO getKeynoteById(Long id);

    Page<KeynoteResponseDTO> getAllKeynotes(String keyword, Pageable pageable);
}
