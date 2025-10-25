package com.dns.controller;

import com.dns.dto.DonationDTO;
import com.dns.service.DonationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/donors/{donorId}/donations")
@RequiredArgsConstructor
public class DonationController {

    private final DonationService donationService;

    @GetMapping
    public ResponseEntity<List<DonationDTO>> getMyDonations(@PathVariable Long donorId) {
        List<DonationDTO> donations = donationService.getDonationsByDonor(donorId);
        return ResponseEntity.ok(donations);
    }
}
