package com.dns.controller;

import com.dns.dto.CampaignDTO;
import com.dns.repository.entity.enums.CampaignType;
import com.dns.service.CampaignService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/public/campaigns")
@RequiredArgsConstructor
public class PublicCampaignController {

    private final CampaignService campaignService;

    // ✅ Get all active campaigns
    @GetMapping
    public ResponseEntity<List<CampaignDTO>> getAllCampaigns() {
        List<CampaignDTO> campaigns = campaignService.getAllCampaigns();
        return ResponseEntity.ok(campaigns);
    }

    // ✅ Get single campaign by ID
    @GetMapping("/{id}")
    public ResponseEntity<CampaignDTO> getCampaignById(@PathVariable Long id) {
        CampaignDTO campaign = campaignService.getCampaignById(id);
        return ResponseEntity.ok(campaign);
    }

    // ✅ Optional: Filter by type
    @GetMapping("/type/{type}")
    public ResponseEntity<List<CampaignDTO>> getCampaignsByType(@PathVariable CampaignType type) {
        List<CampaignDTO> campaigns = campaignService.getCampaignsByType(type);
        return ResponseEntity.ok(campaigns);
    }

    // ✅ Optional: Search by location
    @GetMapping("/location/{location}")
    public ResponseEntity<List<CampaignDTO>> getCampaignsByLocation(@PathVariable String location) {
        List<CampaignDTO> campaigns = campaignService.getCampaignsByLocation(location);
        return ResponseEntity.ok(campaigns);
    }
}
