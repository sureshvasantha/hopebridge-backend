package com.dns.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CampaignImageDTO {

    private Long imageId;
    private String imageUrl;
    private String description;
    private Long campaignId;

    public CampaignImageDTO(Long imageId, String imageUrl, String description) {
        this.imageId = imageId;
        this.imageUrl = imageUrl;
        this.description = description;
    }

}
