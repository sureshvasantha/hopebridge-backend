package com.dns.repository.entity;

import jakarta.persistence.*;
import lombok.*;

@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "campaign_images")
public class CampaignImage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long imageId;

    private String imageUrl;
    private String description;

    @ManyToOne
    @JoinColumn(name = "campaign_id")
    private Campaign campaign;
}
