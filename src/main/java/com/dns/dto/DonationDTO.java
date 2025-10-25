package com.dns.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DonationDTO {
    private Long donationId;
    private Double amount;
    private String paymentId;
    private String status;
    private LocalDateTime donationDate;
    private Long donorId;
    private Long campaignId;
}
