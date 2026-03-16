
package com.conferencehub.keynote.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class KeynoteResponseDTO {

    Long id;
    String nom;
    String prenom;
    String email;
    String fonction;
}
