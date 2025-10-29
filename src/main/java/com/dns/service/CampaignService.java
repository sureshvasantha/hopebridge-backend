package com.dns.service;

import com.dns.dto.CampaignDTO;
import com.dns.dto.DonationDTO;
import com.dns.repository.entity.enums.CampaignStatus;
import com.dns.repository.entity.enums.CampaignType;

import java.io.IOException;
import java.util.List;
import org.springframework.web.multipart.MultipartFile;

public interface CampaignService {

    // CRUD (ADMIN)
    CampaignDTO createCampaign(Long adminId, String campaignJson, List<MultipartFile> imageFiles) throws IOException;

    CampaignDTO updateCampaign(Long adminId, Long campaignId, String campaignJson, List<MultipartFile> imageFiles)
            throws IOException;

    CampaignDTO updateCampaignStatus(Long adminId, Long campaignId, CampaignStatus status);

    List<DonationDTO> getDonationsByCampaign(Long adminId, Long campaignId);

    // Get (Public)
    List<CampaignDTO> getAllCampaigns();

    CampaignDTO getCampaignById(Long id);

    List<CampaignDTO> getCampaignsByType(CampaignType type);

    List<CampaignDTO> getCampaignsByLocation(String location);

    // Get (Admin)
    List<CampaignDTO> getCampaignsByAdmin(Long adminId);

    CampaignDTO getCampaignByAdminAndId(Long adminId, Long campaignId);

    List<CampaignDTO> searchActiveCampaigns(CampaignType type, String location, String keyword);
}
