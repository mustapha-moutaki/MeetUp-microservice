package com.conferencehub.keynote.repository;

import com.conferencehub.keynote.entity.Keynote;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface KeynoteRepository extends JpaRepository<Keynote, Long> {
    Page<Keynote> findByNomContainingOrPrenomContainingOrFonctionContaining(
        String nom, String prenom, String fonction, Pageable pageable);
    boolean existsByEmail(String email);
}