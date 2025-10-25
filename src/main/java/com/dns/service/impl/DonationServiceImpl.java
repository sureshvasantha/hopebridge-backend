package com.dns.service.impl;

import com.dns.repository.DonationRepository;
import com.dns.repository.entity.Donation;
import com.dns.service.DonationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DonationServiceImpl implements DonationService {

    private final DonationRepository donationRepository;

    @Override
    public Donation createDonation(Donation donation) {
        return donationRepository.save(donation);
    }

    @Override
    public List<Donation> getAllDonations() {
        return donationRepository.findAll();
    }

    @Override
    public List<Donation> getDonationsByUser(Long userId) {
        return donationRepository.findByDonor_UserId(userId);
    }

    @Override
    public List<Donation> getDonationsByCampaign(Long campaignId) {
        return donationRepository.findByCampaign_CampaignId(campaignId);
    }
}
