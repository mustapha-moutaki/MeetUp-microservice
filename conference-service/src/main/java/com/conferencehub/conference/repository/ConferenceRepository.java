package com.conferencehub.conference.repository;

import com.conferencehub.conference.entity.Conference;
import com.conferencehub.conference.enums.ConferenceType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ConferenceRepository extends JpaRepository<Conference, Long> {
    Page<Conference> findByTitreContainingIgnoreCaseOrType(String titre, ConferenceType type, Pageable pageable);

    @Query("SELECT CASE WHEN COUNT(c) > 0 THEN true ELSE false END FROM Conference c JOIN c.keynoteIds k WHERE k = :keynoteId")
    boolean existsByKeynoteId(@Param("keynoteId") Long keynoteId);
}
