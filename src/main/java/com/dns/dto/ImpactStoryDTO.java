package com.dns.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ImpactStoryDTO {
    private Long storyId;
    private String title;
    private String content;
    private LocalDateTime postedDate;
    private Long campaignId;
    private List<ImpactImageDTO> images;
}
