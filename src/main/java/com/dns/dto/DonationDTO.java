package com.dns.dto;

import lombok.*;
import java.time.LocalDateTime;

import com.dns.repository.entity.enums.DonationStatus;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DonationDTO {
    private Long donationId;
    private Double amount;
    private DonationStatus status;
    private String currency;
    private Double displayAmount;
    private String displayCurrency;
    private String paymentSessionId;
    private String receiptUrl;
    private LocalDateTime donationDate;
    private Long donorId;
    private String donorName;
    private Long campaignId;
}
