package com.dns.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CampaignDTO {
    private Long campaignId;
    private String title;
    private String description;
    private Double goalAmount;
    private Double collectedAmount;
    private LocalDate startDate;
    private LocalDate endDate;
    private String status;
    private String campaignType;
    private String location;
    private Long createdBy; // user_id reference
    private List<CampaignImageDTO> images;
}
