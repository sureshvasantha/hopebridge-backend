package com.dns.service.impl;

import com.dns.dto.DonationDTO;
import com.dns.dto.StripeCheckoutRequest;
import com.dns.dto.StripeResponse;
import com.dns.exception.DonationAlreadyConfirmedException;
import com.dns.exception.ResourceNotFoundException;
import com.dns.repository.CampaignRepository;
import com.dns.repository.DonationRepository;
import com.dns.repository.UserRepository;
import com.dns.repository.entity.Campaign;
import com.dns.repository.entity.Donation;
import com.dns.repository.entity.User;
import com.dns.repository.entity.enums.DonationStatus;
import com.dns.service.DonationService;
import com.dns.service.StripeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.Charge;
import com.stripe.model.PaymentIntent;
import com.stripe.model.PaymentMethod;
import com.stripe.model.checkout.Session;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class DonationServiceImpl implements DonationService {

    private final CurrencyConversionService conversionService;
    private final StripeService stripeService;
    private final DonationRepository donationRepository;
    private final CampaignRepository campaignRepository;
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;

    @Value("${stripe.secret.key}")
    private String stripeSecretKey;

    @Override
    public List<DonationDTO> getDonationsByDonor(Long donorId) {
        List<Donation> donations = donationRepository.findByDonor_UserId(donorId);
        return donations.stream().map(donation -> {
            DonationDTO dto = modelMapper.map(donation, DonationDTO.class);
            return dto;
        }).collect(Collectors.toList());
    }

    // ✅ 1. Get all donations
    @Override
    public List<DonationDTO> getAllDonations() {
        return donationRepository.findAll().stream()
                .map(donation -> modelMapper.map(donation, DonationDTO.class))
                .collect(Collectors.toList());
    }

    // Get donations by campaign
    @Override
    public List<DonationDTO> getDonationsByCampaign(Long campaignId) {
        return donationRepository.findByCampaign_CampaignId(campaignId).stream()
                .map(donation -> modelMapper.map(donation, DonationDTO.class))
                .collect(Collectors.toList());
    }

    // Create donation manually (non-payment flow)
    @Override
    public Donation createDonation(Donation donation) {
        return donationRepository.save(donation);
    }

    @Override
    public StripeResponse createCheckoutSession(StripeCheckoutRequest request) {
        // Validate entities
        User donor = userRepository.findById(request.getDonorId())
                .orElseThrow(() -> new ResourceNotFoundException("Donor not found"));
        Campaign campaign = campaignRepository.findById(request.getCampaignId())
                .orElseThrow(() -> new ResourceNotFoundException("Campaign not found"));

        // Create Stripe session
        StripeResponse stripeResponse = stripeService.createCheckoutSession(request);
        double inrAmount = conversionService.convertToInr(request.getCurrency(), request.getAmount());

        if ("SUCCESS".equals(stripeResponse.getStatus())) {
            Donation donation = Donation.builder()
                    .amount(inrAmount)
                    .currency("INR")
                    .displayAmount(request.getAmount())
                    .displayCurrency(request.getCurrency())
                    .status(DonationStatus.PENDING)
                    .paymentSessionId(stripeResponse.getSessionId())
                    .donor(donor)
                    .campaign(campaign)
                    .build();
            donationRepository.save(donation);
        }

        return stripeResponse;
    }

    @Override
    @Transactional
    public void confirmDonation(String sessionId) {
        try {
            Stripe.apiKey = stripeSecretKey;

            // 1️⃣ Retrieve Stripe Checkout Session
            Session session = Session.retrieve(sessionId);

            // 2️⃣ Retrieve PaymentIntent (expand latest_charge)
            String paymentIntentId = session.getPaymentIntent();
            Map<String, Object> params = new HashMap<>();
            params.put("expand", List.of("latest_charge"));
            PaymentIntent paymentIntent = PaymentIntent.retrieve(paymentIntentId, params, null);

            // 3️⃣ Find donation by session ID
            Donation donation = donationRepository.findByPaymentSessionId(sessionId)
                    .orElseThrow(() -> new ResourceNotFoundException("Donation not found for session: " + sessionId));

            if (donation.getStatus() == DonationStatus.SUCCESS) {
                throw new DonationAlreadyConfirmedException(
                        "This donation has already been confirmed and processed successfully.");
            }

            // 4️⃣ Extract charge details
            Charge latestCharge = paymentIntent.getLatestChargeObject();
            String transactionId = (latestCharge != null) ? latestCharge.getId() : null;
            String receiptUrl = (latestCharge != null) ? latestCharge.getReceiptUrl() : null;

            // 5️⃣ Parse PaymentMethod
            String paymentMethodId = paymentIntent.getPaymentMethod();
            String readableMethod = "Unknown";
            String methodType = "unknown";

            if (paymentMethodId != null) {
                try {
                    PaymentMethod pm = PaymentMethod.retrieve(paymentMethodId);
                    methodType = pm.getType();
                    readableMethod = buildReadablePaymentMethod(pm);
                } catch (Exception e) {
                    readableMethod = "Unknown (" + paymentMethodId + ")";
                }
            }

            // 6️⃣ Determine final status
            String stripeStatus = paymentIntent.getStatus();
            DonationStatus finalStatus = DonationStatus.PENDING;

            if ("succeeded".equalsIgnoreCase(stripeStatus)) {
                finalStatus = DonationStatus.SUCCESS;
            } else if ("failed".equalsIgnoreCase(stripeStatus) || "canceled".equalsIgnoreCase(stripeStatus)) {
                finalStatus = DonationStatus.FAILED;
            }

            donation.setStatus(finalStatus);
            donation.setPaymentIntentId(paymentIntentId);
            donation.setPaymentMethod(readableMethod);
            donation.setPaymentMethodType(methodType);
            donation.setTransactionId(transactionId);
            donation.setReceiptUrl(receiptUrl);

            donationRepository.save(donation);

            if (finalStatus == DonationStatus.SUCCESS) {
                Campaign campaign = donation.getCampaign();
                if (campaign != null) {
                    Double currentCollected = campaign.getCollectedAmount() != null
                            ? campaign.getCollectedAmount()
                            : 0.0D;
                    campaign.setCollectedAmount(currentCollected + donation.getAmount());
                    campaignRepository.save(campaign);

                    log.info("Donation confirmed: {} {} added to campaign '{}'. New total: {} {}",
                            donation.getCurrency(), donation.getAmount(), campaign.getTitle(), donation.getCurrency(),
                            campaign.getCollectedAmount());
                }
            }

        } catch (StripeException e) {
            throw new RuntimeException("Error confirming Stripe payment: " + e.getMessage(), e);
        }
    }

    /**
     * Converts Stripe PaymentMethod object into readable description.
     */
    private String buildReadablePaymentMethod(PaymentMethod pm) {
        if (pm == null || pm.getType() == null)
            return "Unknown";

        String type = pm.getType();
        switch (type) {
            case "card":
                return String.format("%s ending in %s",
                        pm.getCard().getBrand().toUpperCase(),
                        pm.getCard().getLast4());
            case "upi":
                return "UPI Payment";
            case "wallet":
                return "Wallet Payment";
            case "netbanking":
                return "NetBanking Payment";
            default:
                return type.toUpperCase();
        }
    }

    // @Override
    // public void confirmDonation(String sessionId) {
    // try {
    // // 1️⃣ Retrieve the Stripe Checkout Session
    // Session session = Session.retrieve(sessionId);

    // // 2️⃣ Retrieve the PaymentIntent (expand latest_charge)
    // String paymentIntentId = session.getPaymentIntent();

    // Map<String, Object> params = new HashMap<>();
    // params.put("expand", List.of("latest_charge"));

    // PaymentIntent paymentIntent = PaymentIntent.retrieve(paymentIntentId, params,
    // null);

    // // 3️⃣ Get Donation record from DB
    // Donation donation = donationRepository.findByPaymentSessionId(sessionId)
    // .orElseThrow(() -> new ResourceNotFoundException("Donation not found for
    // session: " + sessionId));

    // // 4️⃣ Extract charge info from the expanded PaymentIntent
    // Charge latestCharge = paymentIntent.getLatestChargeObject();

    // String paymentMethod = paymentIntent.getPaymentMethod();
    // String transactionId = latestCharge != null ? latestCharge.getId() : null;
    // String receiptUrl = latestCharge != null ? latestCharge.getReceiptUrl() :
    // null;

    // // 5️⃣ Determine donation status based on Stripe status
    // String stripeStatus = paymentIntent.getStatus();
    // DonationStatus finalStatus = DonationStatus.PENDING;

    // if ("succeeded".equalsIgnoreCase(stripeStatus)) {
    // finalStatus = DonationStatus.SUCCESS;
    // } else if ("failed".equalsIgnoreCase(stripeStatus) ||
    // "canceled".equalsIgnoreCase(stripeStatus)) {
    // finalStatus = DonationStatus.FAILED;
    // }

    // // 6️⃣ Update donation record
    // donation.setStatus(finalStatus);
    // donation.setPaymentIntentId(paymentIntentId);
    // donation.setPaymentMethod(paymentMethod);
    // donation.setTransactionId(transactionId);
    // donation.setReceiptUrl(receiptUrl);

    // donationRepository.save(donation);

    // } catch (StripeException e) {
    // throw new RuntimeException("Error confirming Stripe payment: " +
    // e.getMessage(), e);
    // }
    // }

}
