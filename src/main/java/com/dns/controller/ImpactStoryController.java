package com.dns.controller;

import com.dns.dto.ImpactStoryDTO;
import com.dns.service.ImpactStoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/admins/{adminId}/impact-stories")
@RequiredArgsConstructor
public class ImpactStoryController {

    private final ImpactStoryService impactStoryService;

    @PreAuthorize("hasRole('ADMIN') and #adminId == authentication.principal.userId")
    @PostMapping(value = "/campaign/{campaignId}", consumes = { "multipart/form-data" })
    public ResponseEntity<ImpactStoryDTO> createImpactStory(
            @PathVariable Long adminId,
            @PathVariable Long campaignId,
            @RequestPart("story") String storyJson,
            @RequestPart(value = "images", required = false) List<MultipartFile> imageFiles) throws IOException {

        log.info("Admin ID={} creating impact story for campaign ID={}", adminId, campaignId);
        ImpactStoryDTO created = impactStoryService.createImpactStory(adminId, campaignId, storyJson, imageFiles);
        log.debug("Impact story created successfully by admin ID={} for campaign ID={}", adminId, campaignId);
        return ResponseEntity.ok(created);
    }

    @PreAuthorize("hasRole('ADMIN') and #adminId == authentication.principal.userId")
    @GetMapping
    public ResponseEntity<List<ImpactStoryDTO>> getImpactStoriesByAdmin(@PathVariable Long adminId) {
        log.info("Fetching impact stories for admin ID: {}", adminId);
        List<ImpactStoryDTO> stories = impactStoryService.getImpactStoriesByAdmin(adminId);
        return ResponseEntity.ok(stories);
    }

    @PreAuthorize("hasRole('ADMIN') and #adminId == authentication.principal.userId")
    @GetMapping("/campaign/{campaignId}")
    public ResponseEntity<List<ImpactStoryDTO>> getImpactStoriesByCampaign(
            @PathVariable Long adminId,
            @PathVariable Long campaignId) {
        log.info("Fetching impact stories for campaign ID: {} by admin ID: {}", campaignId, adminId);
        List<ImpactStoryDTO> stories = impactStoryService.getImpactStoriesByCampaignAndAdmin(adminId, campaignId);
        return ResponseEntity.ok(stories);
    }
}
