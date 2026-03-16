package com.conferencehub.conference.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class KeynoteDTO {
    private Long id;
    private String nom;
    private String prenom;
    private String email;
    private String fonction;
}
