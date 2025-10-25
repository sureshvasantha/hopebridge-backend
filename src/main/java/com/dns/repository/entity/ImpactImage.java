package com.dns.repository.entity;

import jakarta.persistence.*;
import lombok.*;

@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "impact_images")
public class ImpactImage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long imageId;

    private String imageUrl;

    @ManyToOne
    @JoinColumn(name = "story_id")
    private ImpactStory story;
}
