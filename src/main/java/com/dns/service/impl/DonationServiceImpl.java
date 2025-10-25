package com.dns.service.impl;

import com.dns.dto.DonationDTO;
import com.dns.repository.DonationRepository;
import com.dns.repository.entity.Donation;
import com.dns.service.DonationService;
import lombok.RequiredArgsConstructor;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DonationServiceImpl implements DonationService {

    private final DonationRepository donationRepository;
    private final ModelMapper modelMapper;

    @Override
    public List<DonationDTO> getDonationsByDonor(Long donorId) {
        List<Donation> donations = donationRepository.findByDonor_UserId(donorId);
        return donations.stream()
                .map(d -> modelMapper.map(d, DonationDTO.class))
                .collect(Collectors.toList());
    }

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
