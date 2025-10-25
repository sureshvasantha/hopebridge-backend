package com.dns.service.impl;

import com.dns.dto.ImpactImageDTO;
import com.dns.dto.ImpactStoryDTO;
import com.dns.exception.FileStorageException;
import com.dns.exception.ResourceNotFoundException;
import com.dns.exception.UnauthorizedActionException;
import com.dns.repository.CampaignRepository;
import com.dns.repository.DonationRepository;
import com.dns.repository.ImpactStoryRepository;
import com.dns.repository.UserRepository;
import com.dns.repository.entity.Campaign;
import com.dns.repository.entity.ImpactImage;
import com.dns.repository.entity.ImpactStory;
import com.dns.repository.entity.User;
import com.dns.service.ImpactStoryService;
import com.dns.service.S3Service;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.*;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ImpactStoryServiceImpl implements ImpactStoryService {

    private final ImpactStoryRepository impactStoryRepository;
    private final CampaignRepository campaignRepository;
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final ObjectMapper objectMapper;
    private final ImpactStoryRepository storyRepository;
    private final DonationRepository donationRepository;
    private final S3Service s3Service;

    @Override
    public ImpactStoryDTO createImpactStory(Long adminId, Long campaignId, String storyJson,
            List<MultipartFile> imageFiles)
            throws IOException {

        User admin = userRepository.findById(adminId)
                .orElseThrow(() -> new ResourceNotFoundException("Admin not found with ID: " + adminId));

        Campaign campaign = campaignRepository.findById(campaignId)
                .orElseThrow(() -> new ResourceNotFoundException("Campaign not found with ID: " + campaignId));

        // ✅ Ownership validation
        if (!campaign.getCreatedBy().getUserId().equals(adminId)) {
            throw new UnauthorizedActionException("You are not authorized to post impact story for this campaign");
        }

        // ✅ Parse JSON to DTO
        ImpactStoryDTO storyDTO = objectMapper.readValue(storyJson, ImpactStoryDTO.class);

        // ✅ Map to entity
        ImpactStory story = modelMapper.map(storyDTO, ImpactStory.class);
        story.setCampaign(campaign);
        story.setPostedDate(LocalDateTime.now());

        // ✅ Handle image upload
        List<ImpactImage> imageEntities = new ArrayList<>();
        if (imageFiles != null && !imageFiles.isEmpty()) {
            for (MultipartFile file : imageFiles) {
                String imageUrl = s3Service.uploadFile(file, "impact-stories");
                ImpactImage img = new ImpactImage();
                img.setImageUrl(imageUrl);
                img.setStory(story);
                imageEntities.add(img);
            }
        }
        story.setImages(imageEntities);

        ImpactStory saved = impactStoryRepository.save(story);

        // Return DTO response
        ImpactStoryDTO response = modelMapper.map(saved, ImpactStoryDTO.class);
        response.setImages(saved.getImages().stream()
                .map(img -> new ImpactImageDTO(img.getImageId(), img.getImageUrl(), img.getStory().getStoryId()))
                .collect(Collectors.toList()));

        return response;
    }

    @Override
    public List<ImpactStoryDTO> getImpactStoriesByCampaign(Long campaignId) {
        List<ImpactStory> stories = impactStoryRepository.findByCampaign_CampaignId(campaignId);
        return stories.stream()
                .map(story -> modelMapper.map(story, ImpactStoryDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public List<ImpactStoryDTO> getStoriesByCampaignId(Long campaignId) {
        List<ImpactStory> stories = storyRepository.findByCampaign_CampaignId(campaignId);
        return stories.stream()
                .map(story -> modelMapper.map(story, ImpactStoryDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public List<ImpactStoryDTO> getStoriesByDonor(Long donorId) {
        List<ImpactStory> stories = storyRepository.findByCampaign_CampaignIdIn(
                donationRepository.findCampaignIdsByDonor(donorId));
        return stories.stream()
                .map(story -> modelMapper.map(story, ImpactStoryDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public ImpactStoryDTO getImpactStoryById(Long storyId) {
        ImpactStory story = impactStoryRepository.findById(storyId)
                .orElseThrow(() -> new ResourceNotFoundException("Impact story not found with ID: " + storyId));
        return modelMapper.map(story, ImpactStoryDTO.class);
    }

    @Override
    public List<ImpactStoryDTO> getImpactStoriesByAdmin(Long adminId) {
        List<ImpactStory> stories = impactStoryRepository.findByCampaign_CreatedBy_UserId(adminId);
        return stories.stream()
                .map(story -> modelMapper.map(story, ImpactStoryDTO.class))
                .collect(Collectors.toList());
    }
}
