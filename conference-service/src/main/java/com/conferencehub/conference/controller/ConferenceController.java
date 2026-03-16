package com.conferencehub.conference.controller;

import com.conferencehub.conference.dto.ConferenceRequestDTO;
import com.conferencehub.conference.dto.ConferenceResponseDTO;
import com.conferencehub.conference.enums.ConferenceStatut;
import com.conferencehub.conference.enums.ConferenceType;
import com.conferencehub.conference.service.ConferenceService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/conferences")
public class ConferenceController {

    private final ConferenceService service;

    public ConferenceController(ConferenceService service) {
        this.service = service;
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<ConferenceResponseDTO> create(@RequestBody @Valid ConferenceRequestDTO dto) {
        return new ResponseEntity<>(service.createConference(dto), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<ConferenceResponseDTO> update(@PathVariable Long id,
            @RequestBody @Valid ConferenceRequestDTO dto) {
        return ResponseEntity.ok(service.updateConference(id, dto));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.deleteConference(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ConferenceResponseDTO> getById(@PathVariable Long id) {
        return ResponseEntity.ok(service.getConferenceById(id));
    }

    @GetMapping
    public ResponseEntity<Page<ConferenceResponseDTO>> getAll(
            @RequestParam(required = false) String titre,
            @RequestParam(required = false) ConferenceType type,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(service.getAllConferences(titre, type, PageRequest.of(page, size)));
    }

    @PatchMapping("/{id}/statut")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<ConferenceResponseDTO> updateStatut(@PathVariable Long id,
            @RequestParam ConferenceStatut statut) {
        return ResponseEntity.ok(service.updateStatut(id, statut));
    }

    @GetMapping("/exists/keynote/{keynoteId}")
    public ResponseEntity<Boolean> existsByKeynoteId(@PathVariable Long keynoteId) {
        return ResponseEntity.ok(service.existsByKeynoteId(keynoteId));
    }
}
