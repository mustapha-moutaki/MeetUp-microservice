package com.conferencehub.conference.dto;

import com.conferencehub.conference.enums.ConferenceStatut;
import com.conferencehub.conference.enums.ConferenceType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ConferenceResponseDTO {
    private Long id;
    private String titre;
    private ConferenceType type;
    private ConferenceStatut statut;
    private LocalDate date;
    private Integer duree;
    private Integer nombreInscrits;
    private Double score;
    private List<KeynoteDTO> keynotes;
}
