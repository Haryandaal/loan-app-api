package com.loan.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LoanResponse {

    private String id;
    private String customerId;
    private Long amount;
    private Integer duration;
    private double interestRate;
    private String approvalStatus;
    private String rejectionReason;
    private String applicationDate;
}
