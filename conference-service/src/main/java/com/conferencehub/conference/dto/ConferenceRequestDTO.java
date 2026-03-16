package com.conferencehub.conference.dto;

import com.conferencehub.conference.enums.ConferenceType;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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
public class ConferenceRequestDTO {
    @NotBlank
    private String titre;
    @NotNull
    private ConferenceType type;
    @Future
    @NotNull
    private LocalDate date;
    @Min(1)
    private Integer duree;
    private List<Long> keynoteIds;
}
