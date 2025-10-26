package com.dns.repository.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;

import com.dns.repository.entity.enums.DonationStatus;

@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
@Table(name = "donations")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Donation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long donationId;

    private Double amount;

    private String currency;

    @Enumerated(EnumType.STRING)
    private DonationStatus status;

    @Column(name = "payment_session_id")
    private String paymentSessionId;

    @Column(name = "payment_intent_id")
    private String paymentIntentId;

    @Column(name = "payment_method")
    private String paymentMethod;

    @Column(name = "payment_method_type")
    private String paymentMethodType; // e.g. "card", "upi", "wallet"

    @Column(name = "transaction_id")
    private String transactionId;

    @Column(name = "receipt_url")
    private String receiptUrl;

    @CreationTimestamp
    private LocalDateTime donationDate;

    @ManyToOne
    @JoinColumn(name = "donor_id")
    private User donor;

    @ManyToOne
    @JoinColumn(name = "campaign_id")
    private Campaign campaign;
}
