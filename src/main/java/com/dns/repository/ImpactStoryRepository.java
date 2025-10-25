package com.dns.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.dns.repository.entity.ImpactStory;

import java.util.List;

@Repository
public interface ImpactStoryRepository extends JpaRepository<ImpactStory, Long> {
    List<ImpactStory> findByCampaignCampaignId(Long campaignId);

    List<ImpactStory> findByCampaign_CampaignId(Long campaignId);
}
