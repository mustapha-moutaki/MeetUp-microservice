package com.conferencehub.keynote.controller;

import com.conferencehub.keynote.dto.KeynoteRequestDTO;
import com.conferencehub.keynote.dto.KeynoteResponseDTO;
import com.conferencehub.keynote.service.KeynoteService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/keynotes")
public class KeynoteController {

    private final KeynoteService service;

    public KeynoteController(KeynoteService service) {
        this.service = service;
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<KeynoteResponseDTO> create(@RequestBody @Valid KeynoteRequestDTO dto) {
        return new ResponseEntity<>(service.createKeynote(dto), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<KeynoteResponseDTO> update(@PathVariable Long id, @RequestBody @Valid KeynoteRequestDTO dto) {
        return ResponseEntity.ok(service.updateKeynote(id, dto));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.deleteKeynote(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<KeynoteResponseDTO> getById(@PathVariable Long id) {
        return ResponseEntity.ok(service.getKeynoteById(id));
    }

    @GetMapping
    public ResponseEntity<Page<KeynoteResponseDTO>> getAll(
            @RequestParam(required = false) String keyword,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(service.getAllKeynotes(keyword, PageRequest.of(page, size)));
    }
}
