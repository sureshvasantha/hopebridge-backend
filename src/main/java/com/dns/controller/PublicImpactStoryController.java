package com.dns.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dns.dto.ImpactStoryDTO;
import com.dns.service.ImpactStoryService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/public")
@RequiredArgsConstructor
public class PublicImpactStoryController {

    private final ImpactStoryService impactStoryService;

    @GetMapping("/campaigns/{campaignId}/impact-stories")
    public ResponseEntity<List<ImpactStoryDTO>> getStoriesByCampaign(@PathVariable Long campaignId) {
        List<ImpactStoryDTO> stories = impactStoryService.getImpactStoriesByCampaign(campaignId);
        return ResponseEntity.ok(stories);
    }

    @GetMapping("/impact-stories/{storyId}")
    public ResponseEntity<ImpactStoryDTO> getStoryById(@PathVariable Long storyId) {
        ImpactStoryDTO story = impactStoryService.getImpactStoryById(storyId);
        return ResponseEntity.ok(story);
    }
}
