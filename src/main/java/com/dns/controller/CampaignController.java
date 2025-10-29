package com.dns.controller;

import com.dns.dto.CampaignDTO;
import com.dns.dto.DonationDTO;
import com.dns.repository.entity.enums.CampaignStatus;
import com.dns.service.CampaignService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/admins/{adminId}/campaigns")
@RequiredArgsConstructor
public class CampaignController {

    private final CampaignService campaignService;

    @PreAuthorize("hasRole('ADMIN') and #adminId == authentication.principal.userId")
    @GetMapping
    public ResponseEntity<List<CampaignDTO>> getCampaignsByAdmin(@PathVariable Long adminId) {
        log.info("Fetching campaigns for admin ID: {}", adminId);
        List<CampaignDTO> campaigns = campaignService.getCampaignsByAdmin(adminId);
        return ResponseEntity.ok(campaigns);
    }

    @PreAuthorize("hasRole('ADMIN') and #adminId == authentication.principal.userId")
    @GetMapping("/{campaignId}")
    public ResponseEntity<CampaignDTO> getCampaignByAdminAndId(
            @PathVariable Long adminId,
            @PathVariable Long campaignId) {

        log.info("Fetching campaign ID: {} for admin ID: {}", campaignId, adminId);
        CampaignDTO campaign = campaignService.getCampaignByAdminAndId(adminId, campaignId);
        return ResponseEntity.ok(campaign);
    }

    @PreAuthorize("hasRole('ADMIN') and #adminId == authentication.principal.userId")
    @PostMapping(consumes = { "multipart/form-data" })
    public ResponseEntity<CampaignDTO> createCampaign(
            @PathVariable Long adminId,
            @RequestPart("campaign") String campaignJson,
            @RequestPart(value = "images", required = false) List<MultipartFile> images) throws IOException {

        log.info("Creating campaign by admin ID: {}", adminId);
        CampaignDTO created = campaignService.createCampaign(adminId, campaignJson, images);
        log.debug("Campaign created successfully: {}", created);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    @PreAuthorize("hasRole('ADMIN') and #adminId == authentication.principal.userId")
    @PutMapping(value = "/{campaignId}", consumes = { "multipart/form-data" })
    public ResponseEntity<CampaignDTO> updateCampaign(
            @PathVariable Long adminId,
            @PathVariable Long campaignId,
            @RequestPart("campaignJson") String campaignJson,
            @RequestPart(value = "imageFiles", required = false) List<MultipartFile> imageFiles) throws IOException {

        log.info("Updating campaign ID: {} by admin ID: {}", campaignId, adminId);
        CampaignDTO updated = campaignService.updateCampaign(adminId, campaignId, campaignJson, imageFiles);
        log.debug("Campaign updated: {}", updated);
        return ResponseEntity.ok(updated);
    }

    @PreAuthorize("hasRole('ADMIN') and #adminId == authentication.principal.userId")
    @PatchMapping("/{campaignId}/status")
    public ResponseEntity<CampaignDTO> updateCampaignStatus(
            @PathVariable Long adminId,
            @PathVariable Long campaignId,
            @RequestParam CampaignStatus status) {

        log.info("Updating campaign status for ID: {} by admin ID: {} to {}", campaignId, adminId, status);
        CampaignDTO updated = campaignService.updateCampaignStatus(adminId, campaignId, status);
        return ResponseEntity.ok(updated);
    }

    @PreAuthorize("hasRole('ADMIN') and #adminId == authentication.principal.userId")
    @GetMapping("/{campaignId}/donations")
    public ResponseEntity<List<DonationDTO>> getDonationsByCampaign(
            @PathVariable Long adminId, @PathVariable Long campaignId) {

        log.info("Fetching donations for campaign ID: {} by admin ID: {}", campaignId, adminId);
        return ResponseEntity.ok(campaignService.getDonationsByCampaign(adminId, campaignId));
    }
}
