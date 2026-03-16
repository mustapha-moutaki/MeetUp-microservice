package com.conferencehub.conference.entity;

import com.conferencehub.conference.enums.ConferenceStatut;
import com.conferencehub.conference.enums.ConferenceType;
import jakarta.persistence.*;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "conferences")
public class Conference {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private String titre;

    @Enumerated(EnumType.STRING)
    private ConferenceType type;

    @Enumerated(EnumType.STRING)
    @Builder.Default
    private ConferenceStatut statut = ConferenceStatut.PLANIFIEE;

    @Future
    private LocalDate date;

    @Min(1)
    private Integer duree;

    @Builder.Default
    private Integer nombreInscrits = 0;

    @Builder.Default
    private Double score = 0.0;

    @ElementCollection
    @CollectionTable(name = "conference_keynotes", joinColumns = @JoinColumn(name = "conference_id"))
    @Column(name = "keynote_id")
    @Builder.Default
    private List<Long> keynoteIds = new ArrayList<>();

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @PrePersist
    void onCreate() {
        createdAt = updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
