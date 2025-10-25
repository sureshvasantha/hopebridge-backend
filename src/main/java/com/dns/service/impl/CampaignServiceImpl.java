package com.dns.service.impl;

import com.dns.dto.CampaignDTO;
import com.dns.dto.CampaignImageDTO;
import com.dns.dto.DonationDTO;
import com.dns.exception.FileStorageException;
import com.dns.exception.ResourceNotFoundException;
import com.dns.exception.UnauthorizedActionException;
import com.dns.repository.CampaignRepository;
import com.dns.repository.UserRepository;
import com.dns.repository.entity.Campaign;
import com.dns.repository.entity.CampaignImage;
import com.dns.repository.entity.User;
import com.dns.repository.entity.enums.CampaignStatus;
import com.dns.repository.entity.enums.CampaignType;
import com.dns.service.CampaignService;
import com.dns.service.S3Service;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.*;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CampaignServiceImpl implements CampaignService {

    private final CampaignRepository campaignRepository;
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final ObjectMapper objectMapper;
    private final S3Service s3Service;

    private static final String UPLOAD_DIR = System.getProperty("user.dir") + "/uploads/campaigns/";

    @Override
    public CampaignDTO createCampaign(Long adminId, String campaignJson, List<MultipartFile> imageFiles)
            throws IOException {

        User admin = userRepository.findById(adminId)
                .orElseThrow(() -> new ResourceNotFoundException("Admin not found with ID: " + adminId));

        // Parse JSON into DTO
        CampaignDTO campaignDTO = objectMapper.readValue(campaignJson, CampaignDTO.class);

        // Map DTO to entity
        Campaign campaign = modelMapper.map(campaignDTO, Campaign.class);
        campaign.setCreatedBy(admin);
        campaign.setCollectedAmount(0.0);
        campaign.setStatus(CampaignStatus.ACTIVE);

        if (campaignDTO.getStartDate() != null)
            campaign.setStartDate(campaignDTO.getStartDate());
        else
            campaign.setStartDate(LocalDate.now());

        if (campaignDTO.getEndDate() != null)
            campaign.setEndDate(campaignDTO.getEndDate());
        else
            campaign.setEndDate(LocalDate.now());

        List<CampaignImage> images = new ArrayList<>();

        if (imageFiles != null && !imageFiles.isEmpty()) {
            for (MultipartFile file : imageFiles) {
                String fileUrl = s3Service.uploadFile(file, "campaigns");
                CampaignImage img = new CampaignImage();
                img.setImageUrl(fileUrl);
                img.setCampaign(campaign);
                images.add(img);
            }
        }

        campaign.setImages(images);
        Campaign saved = campaignRepository.save(campaign);

        // ✅ Convert back to DTO for response
        CampaignDTO response = modelMapper.map(saved, CampaignDTO.class);
        response.setImages(
                saved.getImages().stream()
                        .map(img -> new CampaignImageDTO(img.getImageId(), img.getImageUrl(), img.getDescription()))
                        .collect(Collectors.toList()));

        return response;
    }

    @Override
    public CampaignDTO updateCampaign(Long adminId, Long campaignId, String campaignJson,
            List<MultipartFile> imageFiles)
            throws IOException {

        // Parse JSON to DTO
        CampaignDTO campaignDTO = objectMapper.readValue(campaignJson, CampaignDTO.class);

        Campaign existing = campaignRepository.findById(campaignId)
                .orElseThrow(() -> new ResourceNotFoundException("Campaign not found with ID: " + campaignId));

        // Validate that admin owns the campaign (to be activated when JWT added)
        if (!existing.getCreatedBy().getUserId().equals(adminId)) {
            throw new UnauthorizedActionException("You are not authorized to update this campaign");
        }

        if (existing.getCollectedAmount() > 0 ||
                (existing.getStartDate() != null && existing.getStartDate().isBefore(LocalDate.now()))) {

            campaignDTO.setStartDate(existing.getStartDate());
            campaignDTO.setCollectedAmount(existing.getCollectedAmount());
        }

        // Update editable fields only
        existing.setTitle(campaignDTO.getTitle());
        existing.setDescription(campaignDTO.getDescription());
        existing.setGoalAmount(campaignDTO.getGoalAmount());
        existing.setEndDate(campaignDTO.getEndDate());
        existing.setCampaignType(campaignDTO.getCampaignType());
        existing.setLocation(campaignDTO.getLocation());
        existing.setStatus(campaignDTO.getStatus());

        // Delete old images from S3 and DB
        if (existing.getImages() != null) {
            for (CampaignImage img : existing.getImages()) {
                s3Service.deleteFile(img.getImageUrl());
            }
            existing.getImages().clear();
        }

        // Upload new images
        if (imageFiles != null && !imageFiles.isEmpty()) {
            for (MultipartFile file : imageFiles) {
                String fileUrl = s3Service.uploadFile(file, "campaigns");
                CampaignImage img = new CampaignImage();
                img.setImageUrl(fileUrl);
                img.setCampaign(existing);
                existing.getImages().add(img);
            }
        }

        Campaign updated = campaignRepository.save(existing);

        return modelMapper.map(updated, CampaignDTO.class);
    }

    @Override
    public CampaignDTO updateCampaignStatus(Long adminId, Long campaignId, CampaignStatus status) {
        Campaign campaign = campaignRepository.findById(campaignId)
                .orElseThrow(() -> new ResourceNotFoundException("Campaign not found with ID: " + campaignId));

        if (!campaign.getCreatedBy().getUserId().equals(adminId)) {
            throw new UnauthorizedActionException("You are not authorized to change this campaign’s status");
        }

        campaign.setStatus(status);
        Campaign updated = campaignRepository.save(campaign);
        return modelMapper.map(updated, CampaignDTO.class);
    }

    @Override
    public List<DonationDTO> getDonationsByCampaign(Long adminId, Long campaignId) {
        Campaign campaign = campaignRepository.findById(campaignId)
                .orElseThrow(() -> new ResourceNotFoundException("Campaign not found with ID: " + campaignId));

        if (!campaign.getCreatedBy().getUserId().equals(adminId)) {
            throw new UnauthorizedActionException("Not authorized to view donations for this campaign");
        }

        return campaign.getDonations().stream()
                .map(donation -> modelMapper.map(donation, DonationDTO.class))
                .collect(Collectors.toList());
    }

    // -------------------- GET (Admin) --------------------
    @Override
    public List<CampaignDTO> getCampaignsByAdmin(Long adminId) {
        List<Campaign> campaigns = campaignRepository.findByCreatedBy_UserId(adminId);
        return campaigns.stream()
                .map(campaign -> modelMapper.map(campaign, CampaignDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public CampaignDTO getCampaignByAdminAndId(Long adminId, Long campaignId) {
        Campaign campaign = campaignRepository.findByCampaignIdAndCreatedBy_UserId(campaignId, adminId)
                .orElseThrow(() -> new RuntimeException("Campaign not found for this admin"));
        return modelMapper.map(campaign, CampaignDTO.class);
    }

    // -------------------- GET (Public) --------------------

    @Override
    public List<CampaignDTO> searchActiveCampaigns(CampaignType type, String location, String keyword) {
        List<Campaign> campaigns = campaignRepository.searchActiveCampaigns(type, location, keyword);
        return campaigns.stream()
                .map(c -> modelMapper.map(c, CampaignDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public List<CampaignDTO> getAllCampaigns() {
        List<Campaign> campaigns = campaignRepository.findAll();
        return campaigns.stream()
                .map(campaign -> modelMapper.map(campaign, CampaignDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public CampaignDTO getCampaignById(Long id) {
        return campaignRepository.findById(id)
                .map(campaign -> modelMapper.map(campaign, CampaignDTO.class))
                .orElseThrow(() -> new ResourceNotFoundException("Campaign not found with id: " + id));
    }

    @Override
    public List<CampaignDTO> getCampaignsByType(CampaignType type) {
        List<Campaign> campaigns = campaignRepository.findByCampaignType(type);
        return campaigns.stream()
                .map(campaign -> modelMapper.map(campaign, CampaignDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public List<CampaignDTO> getCampaignsByLocation(String location) {
        List<Campaign> campaigns = campaignRepository.findByLocationIgnoreCase(location);
        return campaigns.stream()
                .map(campaign -> modelMapper.map(campaign, CampaignDTO.class))
                .collect(Collectors.toList());
    }

}
