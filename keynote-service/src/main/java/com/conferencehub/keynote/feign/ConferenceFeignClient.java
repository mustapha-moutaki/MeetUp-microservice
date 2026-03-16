package com.conferencehub.keynote.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "conference-service")
public interface ConferenceFeignClient {

    @GetMapping("/api/conferences/exists/keynote/{keynoteId}")
    Boolean existsByKeynoteId(@PathVariable("keynoteId") Long keynoteId);
}
