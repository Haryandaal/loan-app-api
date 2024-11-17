package com.loan.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CustomerResponse {

    private String id;

    private String name;

    private String address;

    private Double income;

    private Double creditScore;

    private String employmentStatus;

    private String phone;

    private String email;

    private String userId;
}
