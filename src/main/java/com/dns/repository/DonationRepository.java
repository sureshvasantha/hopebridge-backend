package com.dns.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.dns.repository.entity.Donation;
import com.dns.repository.entity.enums.DonationStatus;

import java.util.List;

@Repository
public interface DonationRepository extends JpaRepository<Donation, Long> {
    List<Donation> findByDonorUserId(Long userId);

    List<Donation> findByCampaignCampaignId(Long campaignId);

    List<Donation> findByStatus(DonationStatus status);

    List<Donation> findByDonor_UserId(Long userId);

    List<Donation> findByCampaign_CampaignId(Long campaignId);
}
