package com.dns.service;

import com.dns.dto.DonationDTO;
import com.dns.dto.StripeCheckoutRequest;
import com.dns.dto.StripeResponse;
import com.dns.repository.entity.Donation;
import java.util.List;

public interface DonationService {
    Donation createDonation(Donation donation);

    List<DonationDTO> getAllDonations();

    List<DonationDTO> getDonationsByCampaign(Long campaignId);

    List<DonationDTO> getDonationsByDonor(Long donorId);

    StripeResponse createCheckoutSession(StripeCheckoutRequest request);

    void confirmDonation(String sessionId);

}
