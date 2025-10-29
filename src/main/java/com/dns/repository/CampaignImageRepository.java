package com.dns.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.dns.repository.entity.CampaignImage;

import java.util.List;

@Repository
public interface CampaignImageRepository extends JpaRepository<CampaignImage, Long> {
    List<CampaignImage> findByCampaignCampaignId(Long campaignId);
}
