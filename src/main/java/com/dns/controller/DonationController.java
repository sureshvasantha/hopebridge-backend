package com.dns.controller;

import com.dns.dto.DonationDTO;
import com.dns.dto.StripeCheckoutRequest;
import com.dns.dto.StripeResponse;
import com.dns.service.DonationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/donors/{donorId}/donations")
public class DonationController {

    private final DonationService donationService;

    @PreAuthorize("hasRole('DONOR') and #donorId == authentication.principal.userId")
    @GetMapping
    public ResponseEntity<List<DonationDTO>> getMyDonations(@PathVariable Long donorId) {
        log.info("Fetching donations for donor ID: {}", donorId);
        List<DonationDTO> donations = donationService.getDonationsByDonor(donorId);
        return ResponseEntity.ok(donations);
    }

    @PreAuthorize("hasRole('DONOR') and #donorId == authentication.principal.userId")
    @PostMapping("/checkout")
    public ResponseEntity<StripeResponse> createCheckoutSession(
            @PathVariable Long donorId,
            @RequestBody StripeCheckoutRequest request) {

        log.info("Creating checkout session for donor ID: {}", donorId);
        request.setDonorId(donorId);
        StripeResponse response = donationService.createCheckoutSession(request);
        log.debug("Checkout session created for donor ID: {} with session ID: {}", donorId, response.getSessionId());
        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasRole('DONOR')")
    @GetMapping("/confirm")
    public ResponseEntity<Map<String, String>> confirmPayment(@RequestParam("session_id") String sessionId) {
        log.info("Confirming donation for session ID: {}", sessionId);
        donationService.confirmDonation(sessionId);
        log.info("Donation confirmed successfully for session ID: {}", sessionId);
        Map<String, String> result = new HashMap<>();
        result.put("message", "Donation confirmed successfully.");
        return ResponseEntity.ok(result);
    }
}
