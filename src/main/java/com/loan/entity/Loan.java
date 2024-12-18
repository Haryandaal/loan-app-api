package com.loan.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "loan")
@Builder
public class Loan {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @ManyToOne
    @JoinColumn(name = "customer_id")
    private Customer customer;

    private Long amount;

    @Column(name = "duration_in_months")
    private Integer duration;

    @Column(name = "interest_rate")
    private Double interestRate;

    @Enumerated(EnumType.STRING)
    @Column(name = "approval_status")
    private ApprovalStatus approvalStatus;

    @Column(name = "application_date")
    private LocalDateTime applicationDate;

    @Column(name = "rejection_reason")
    private String rejectionReason;

    @PrePersist
    private void prePersist() {
        this.applicationDate = LocalDateTime.now();
    }
}
