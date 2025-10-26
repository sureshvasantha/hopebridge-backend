package com.dns.controller;

import com.dns.dto.DonationDTO;
import com.dns.dto.StripeCheckoutRequest;
import com.dns.dto.StripeResponse;
import com.dns.service.DonationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/donors/{donorId}/donations")
public class DonationController {

    private final DonationService donationService;

    @GetMapping
    public ResponseEntity<List<DonationDTO>> getMyDonations(@PathVariable Long donorId) {
        List<DonationDTO> donations = donationService.getDonationsByDonor(donorId);
        return ResponseEntity.ok(donations);
    }

    @PostMapping("/checkout")
    public ResponseEntity<StripeResponse> createCheckoutSession(
            @PathVariable Long donorId,
            @RequestBody StripeCheckoutRequest request) {

        request.setDonorId(donorId);
        StripeResponse response = donationService.createCheckoutSession(request);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/confirm")
    public ResponseEntity<String> confirmPayment(@RequestParam("session_id") String sessionId) {
        donationService.confirmDonation(sessionId);
        return ResponseEntity.ok("Donation confirmed successfully.");
    }
}
