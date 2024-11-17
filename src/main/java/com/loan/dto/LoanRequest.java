package com.loan.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LoanRequest {
    private Long amount;
    private Integer duration;
    private Double interestRate;

}
