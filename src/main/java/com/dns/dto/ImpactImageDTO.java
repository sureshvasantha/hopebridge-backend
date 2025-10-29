package com.dns.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ImpactImageDTO {
    private Long imageId;
    private String imageUrl;
    private Long storyId;
}
