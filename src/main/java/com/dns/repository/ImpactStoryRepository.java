package com.dns.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.dns.repository.entity.ImpactStory;

import java.util.List;

@Repository
public interface ImpactStoryRepository extends JpaRepository<ImpactStory, Long> {

    List<ImpactStory> findByCampaign_CampaignId(Long campaignId);

    List<ImpactStory> findByCampaign_CreatedBy_UserId(Long adminId);

    List<ImpactStory> findByCampaign_CampaignIdAndCampaign_CreatedBy_UserId(Long campaignId, Long adminId);

    @Query("SELECT s FROM ImpactStory s WHERE s.campaign.campaignId IN :campaignIds")
    List<ImpactStory> findByCampaign_CampaignIdIn(@Param("campaignIds") List<Long> campaignIds);

}
