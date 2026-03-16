package com.conferencehub.keynote.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class KeynoteRequestDTO {

    @NotBlank String nom;
    @NotBlank String prenom;
    @NotBlank @Email String email;
    @NotBlank String fonction;
}
