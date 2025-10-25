package com.dns.repository.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.dns.repository.entity.enums.CampaignStatus;
import com.dns.repository.entity.enums.CampaignType;

@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "campaigns")
public class Campaign {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long campaignId;

    private String title;

    @Column(length = 5000)
    private String description;

    private Double goalAmount;
    private Double collectedAmount;
    private LocalDate startDate;
    private LocalDate endDate;

    @Enumerated(EnumType.STRING)
    private CampaignStatus status;

    @Enumerated(EnumType.STRING)
    private CampaignType campaignType;

    private String location; // Optional field

    @ManyToOne
    @JoinColumn(name = "created_by")
    private User createdBy;

    @OneToMany(mappedBy = "campaign", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CampaignImage> images = new ArrayList<>();

    @OneToMany(mappedBy = "campaign", cascade = CascadeType.ALL)
    private List<Donation> donations;

    @OneToMany(mappedBy = "campaign", cascade = CascadeType.ALL)
    private List<ImpactStory> impactStories;
}
