package com.conferencehub.conference.feign;

import com.conferencehub.conference.dto.KeynoteDTO;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

@Component
public class KeynoteFeignFallback implements KeynoteFeignClient {

    @Override
    public KeynoteDTO getKeynoteById(Long id) {
        return KeynoteDTO.builder()
                .id(id)
                .nom("Indisponible")
                .prenom("Indisponible")
                .build();
    }

    @Override
    public List<KeynoteDTO> getAllKeynotes() {
        return Collections.emptyList();
    }
}
