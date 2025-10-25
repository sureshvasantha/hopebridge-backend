package com.dns.service.impl;

import com.dns.repository.CampaignRepository;
import com.dns.repository.entity.Campaign;
import com.dns.repository.entity.enums.CampaignType;
import com.dns.service.CampaignService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CampaignServiceImpl implements CampaignService {

    private final CampaignRepository campaignRepository;

    @Override
    public Campaign createCampaign(Campaign campaign) {
        return campaignRepository.save(campaign);
    }

    @Override
    public List<Campaign> getAllCampaigns() {
        return campaignRepository.findAll();
    }

    @Override
    public Optional<Campaign> getCampaignById(Long id) {
        return campaignRepository.findById(id);
    }

    @Override
    public List<Campaign> getCampaignsByType(CampaignType type) {
        return campaignRepository.findByCampaignType(type);
    }

    @Override
    public List<Campaign> getCampaignsByLocation(String location) {
        return campaignRepository.findByLocationContainingIgnoreCase(location);
    }

    @Override
    public Campaign updateCampaign(Long id, Campaign updatedCampaign) {
        return campaignRepository.findById(id)
                .map(campaign -> {
                    campaign.setTitle(updatedCampaign.getTitle());
                    campaign.setDescription(updatedCampaign.getDescription());
                    campaign.setGoalAmount(updatedCampaign.getGoalAmount());
                    campaign.setEndDate(updatedCampaign.getEndDate());
                    campaign.setCampaignType(updatedCampaign.getCampaignType());
                    campaign.setLocation(updatedCampaign.getLocation());
                    return campaignRepository.save(campaign);
                })
                .orElseThrow(() -> new RuntimeException("Campaign not found"));
    }

    @Override
    public void deleteCampaign(Long id) {
        campaignRepository.deleteById(id);
    }
}
