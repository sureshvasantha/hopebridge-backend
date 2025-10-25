package com.dns.service;

import com.dns.repository.entity.Campaign;
import com.dns.repository.entity.enums.CampaignType;
import java.util.List;
import java.util.Optional;

public interface CampaignService {
    Campaign createCampaign(Campaign campaign);

    List<Campaign> getAllCampaigns();

    Optional<Campaign> getCampaignById(Long id);

    List<Campaign> getCampaignsByType(CampaignType type);

    List<Campaign> getCampaignsByLocation(String location);

    Campaign updateCampaign(Long id, Campaign updatedCampaign);

    void deleteCampaign(Long id);
}
