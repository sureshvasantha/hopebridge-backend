package com.dns.controller;

import com.dns.dto.ImpactStoryDTO;
import com.dns.service.ImpactStoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/donors/{donorId}/impact-stories")
@RequiredArgsConstructor
public class DonorImpactStoryController {

    private final ImpactStoryService impactStoryService;

    @GetMapping
    public ResponseEntity<List<ImpactStoryDTO>> getImpactStoriesByDonor(@PathVariable Long donorId) {
        List<ImpactStoryDTO> stories = impactStoryService.getStoriesByDonor(donorId);
        return ResponseEntity.ok(stories);
    }
}
