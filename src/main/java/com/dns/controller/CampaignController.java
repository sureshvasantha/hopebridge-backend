package com.dns.controller;

import com.dns.dto.CampaignDTO;
import com.dns.dto.DonationDTO;
import com.dns.repository.entity.enums.CampaignStatus;
import com.dns.service.CampaignService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/admins/{adminId}/campaigns")
@RequiredArgsConstructor
public class CampaignController {

    private final CampaignService campaignService;

    // ✅ Get all campaigns created by this admin
    @GetMapping
    public ResponseEntity<List<CampaignDTO>> getCampaignsByAdmin(@PathVariable Long adminId) {
        List<CampaignDTO> campaigns = campaignService.getCampaignsByAdmin(adminId);
        return ResponseEntity.ok(campaigns);
    }

    // ✅ Get single campaign by ID (only if owned by this admin)
    @GetMapping("/{campaignId}")
    public ResponseEntity<CampaignDTO> getCampaignByAdminAndId(
            @PathVariable Long adminId,
            @PathVariable Long campaignId) {

        CampaignDTO campaign = campaignService.getCampaignByAdminAndId(adminId, campaignId);
        return ResponseEntity.ok(campaign);
    }

    // @PreAuthorize("#adminId == authentication.principal.id")
    @PostMapping(consumes = { "multipart/form-data" })
    public ResponseEntity<CampaignDTO> createCampaign(
            @PathVariable Long adminId,
            @RequestPart("campaign") String campaignJson,
            @RequestPart(value = "images", required = false) List<MultipartFile> images) throws IOException {

        CampaignDTO created = campaignService.createCampaign(adminId, campaignJson, images);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    // @PreAuthorize("#adminId == authentication.principal.id")
    @PutMapping(value = "/{campaignId}", consumes = { "multipart/form-data" })
    public ResponseEntity<CampaignDTO> updateCampaign(
            @PathVariable Long adminId,
            @PathVariable Long campaignId,
            @RequestPart("campaignJson") String campaignJson,
            @RequestPart(value = "imageFiles", required = false) List<MultipartFile> imageFiles) throws IOException {

        CampaignDTO updated = campaignService.updateCampaign(adminId, campaignId, campaignJson, imageFiles);
        return ResponseEntity.ok(updated);
    }

    // @PreAuthorize("#adminId == authentication.principal.id")
    @PatchMapping("/{adminId}/campaigns/{campaignId}/status")
    public ResponseEntity<CampaignDTO> updateCampaignStatus(
            @PathVariable Long adminId,
            @PathVariable Long campaignId,
            @RequestParam CampaignStatus status) {

        CampaignDTO updated = campaignService.updateCampaignStatus(adminId, campaignId, status);
        return ResponseEntity.ok(updated);
    }

    // @PreAuthorize("#adminId == authentication.principal.id")
    @GetMapping("/{adminId}/campaigns/{campaignId}/donations")
    public ResponseEntity<List<DonationDTO>> getDonationsByCampaign(
            @PathVariable Long adminId, @PathVariable Long campaignId) {
        return ResponseEntity.ok(campaignService.getDonationsByCampaign(adminId, campaignId));
    }

}
