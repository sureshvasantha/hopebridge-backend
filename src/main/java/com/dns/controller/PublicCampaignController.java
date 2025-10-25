package com.dns.controller;

import com.dns.dto.CampaignDTO;
import com.dns.dto.ImpactStoryDTO;
import com.dns.exception.InactiveCampaignAccessException;
import com.dns.repository.entity.enums.CampaignStatus;
import com.dns.repository.entity.enums.CampaignType;
import com.dns.service.CampaignService;
import com.dns.service.ImpactStoryService;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/public/campaigns")
@RequiredArgsConstructor
public class PublicCampaignController {

    private final CampaignService campaignService;
    private final ImpactStoryService impactStoryService;

    /**
     * - Get All active campaigns
     * - Filter by type, location, keyword (optional)
     */
    @GetMapping
    public ResponseEntity<List<CampaignDTO>> searchCampaigns(
            @RequestParam(required = false) CampaignType type,
            @RequestParam(required = false) String location,
            @RequestParam(required = false) String keyword) {

        List<CampaignDTO> campaigns = campaignService.searchActiveCampaigns(type, location, keyword);
        return ResponseEntity.ok(campaigns);
    }

    /**
     * Get campaign details by ID (public view)
     */
    @GetMapping("/{id}")
    public ResponseEntity<CampaignDTO> getCampaignById(@PathVariable Long id) {
        CampaignDTO campaign = campaignService.getCampaignById(id);
        if (campaign.getStatus() != CampaignStatus.ACTIVE) {
            throw new InactiveCampaignAccessException(
                    "This campaign is not active or publicly viewable");
        }
        return ResponseEntity.ok(campaign);
    }
}
