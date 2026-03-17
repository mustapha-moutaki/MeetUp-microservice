package com.conferencehub.keynote.kafka;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class KeynoteEvent {

    private String type;
    private Long keynoteId;
    private String nom;
    private String prenom;
    private String email;
}
