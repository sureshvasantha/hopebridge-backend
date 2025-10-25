package com.dns.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.dns.repository.entity.Campaign;
import com.dns.repository.entity.enums.CampaignStatus;
import com.dns.repository.entity.enums.CampaignType;

import java.util.List;
import java.util.Optional;

@Repository
public interface CampaignRepository extends JpaRepository<Campaign, Long> {
    List<Campaign> findByStatus(CampaignStatus status);

    List<Campaign> findByCampaignType(CampaignType type);

    List<Campaign> findByLocationIgnoreCase(String location);

    List<Campaign> findByCreatedByUserId(Long userId);

    List<Campaign> findByTitleContainingIgnoreCase(String keyword);

    List<Campaign> findByLocationContainingIgnoreCase(String location);

    List<Campaign> findByCreatedBy_UserId(Long adminId);

    Optional<Campaign> findByCampaignIdAndCreatedBy_UserId(Long campaignId, Long adminId);
}
