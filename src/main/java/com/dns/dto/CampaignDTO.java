package com.dns.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

import com.dns.repository.entity.enums.CampaignStatus;
import com.dns.repository.entity.enums.CampaignType;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CampaignDTO {
    private Long campaignId;

    @NotBlank(message = "Campaign title is required")
    private String title;

    @NotBlank(message = "Campaign description is required")
    private String description;

    @NotNull(message = "Goal amount is required")
    @Positive(message = "Goal amount must be positive")
    private Double goalAmount;

    private Double collectedAmount;
    private LocalDate startDate;
    private LocalDate endDate;
    private CampaignStatus status;

    @NotBlank(message = "Campaign type is required")
    private CampaignType campaignType;
    private String location;
    private Long createdBy; // user_id reference
    private List<CampaignImageDTO> images;
    private List<ImpactStoryDTO> stories;
}
