package com.dns.repository.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import com.dns.repository.entity.enums.DonationStatus;

import java.time.LocalDateTime;

@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "donations")
public class Donation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long donationId;

    private Double amount;
    private String paymentId;

    @Enumerated(EnumType.STRING)
    private DonationStatus status;

    @CreationTimestamp
    private LocalDateTime donationDate;

    @ManyToOne
    @JoinColumn(name = "donor_id")
    private User donor;

    @ManyToOne
    @JoinColumn(name = "campaign_id")
    private Campaign campaign;
}
