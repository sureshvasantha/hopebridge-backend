package com.dns.service;

import com.dns.dto.DonationDTO;
import com.dns.repository.entity.Donation;
import java.util.List;

public interface DonationService {
    Donation createDonation(Donation donation);

    List<Donation> getAllDonations();

    List<Donation> getDonationsByUser(Long userId);

    List<Donation> getDonationsByCampaign(Long campaignId);

    List<DonationDTO> getDonationsByDonor(Long donorId);
    
}
