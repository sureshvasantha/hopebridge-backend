package com.dns.controller;

import com.dns.dto.ImpactStoryDTO;
import com.dns.service.ImpactStoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/donors/{donorId}/impact-stories")
@RequiredArgsConstructor
public class DonorImpactStoryController {

    private final ImpactStoryService impactStoryService;

    @PreAuthorize("hasRole('DONOR') and #donorId == authentication.principal.userId")
    @GetMapping
    public ResponseEntity<List<ImpactStoryDTO>> getImpactStoriesByDonor(@PathVariable Long donorId) {
        log.info("Fetching impact stories for donor ID: {}", donorId);
        List<ImpactStoryDTO> stories = impactStoryService.getStoriesByDonor(donorId);
        log.debug("Retrieved {} impact stories for donor ID: {}", stories.size(), donorId);
        return ResponseEntity.ok(stories);
    }
}
