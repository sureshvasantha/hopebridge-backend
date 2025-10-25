package com.dns.service;

import com.dns.dto.ImpactStoryDTO;

import java.io.IOException;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

public interface ImpactStoryService {
    ImpactStoryDTO createImpactStory(Long adminId, Long campaignId, String storyJson, List<MultipartFile> imageFiles)
            throws IOException;

    ImpactStoryDTO getImpactStoryById(Long storyId);

    List<ImpactStoryDTO> getImpactStoriesByAdmin(Long adminId);

    List<ImpactStoryDTO> getStoriesByCampaignId(Long campaignId);

    List<ImpactStoryDTO> getStoriesByDonor(Long donorId);

    List<ImpactStoryDTO> getImpactStoriesByCampaign(Long campaignId);

}
