package com.loan.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;


@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Entity
@Table(name = "customer")
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String address;

    @Column(name = "phone_number", unique = true)
    private String phone;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private Double income;

    @Column(name = "credit_score")
    private Double creditScore;

    @Column(name = "employment_status")
    private String employmentStatus;

    @OneToOne()
    @JoinColumn(name = "user_account_id")
    private UserAccount userAccount;

    @OneToMany(mappedBy = "customer")
    private List<Loan> loan;
}

