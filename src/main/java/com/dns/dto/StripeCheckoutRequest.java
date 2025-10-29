package com.dns.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StripeCheckoutRequest {
    private Long campaignId;
    private Long donorId;
    private Double amount;
    private String currency;
}
