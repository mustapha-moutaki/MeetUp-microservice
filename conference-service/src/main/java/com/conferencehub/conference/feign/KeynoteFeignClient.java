package com.conferencehub.conference.feign;

import com.conferencehub.conference.dto.KeynoteDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(name = "keynote-service", fallback = KeynoteFeignFallback.class)
public interface KeynoteFeignClient {

    @GetMapping("/api/keynotes/{id}")
    KeynoteDTO getKeynoteById(@PathVariable("id") Long id);

    @GetMapping("/api/keynotes")
    List<KeynoteDTO> getAllKeynotes();
}
