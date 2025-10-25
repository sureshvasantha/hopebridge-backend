package com.dns.service;

import com.dns.dto.ImpactStoryDTO;
import com.dns.repository.entity.ImpactStory;

import java.io.IOException;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

public interface ImpactStoryService {
    ImpactStoryDTO createImpactStory(Long adminId, Long campaignId, String storyJson, List<MultipartFile> imageFiles)
            throws IOException;

    List<ImpactStoryDTO> getImpactStoriesByCampaign(Long campaignId);

    ImpactStoryDTO getImpactStoryById(Long storyId);

    List<ImpactStoryDTO> getImpactStoriesByAdmin(Long adminId);
}
