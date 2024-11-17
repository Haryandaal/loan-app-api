package com.loan.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CustomerRequest {

    @NotBlank(message = "username is required")
    private String username;

    @NotBlank(message = "password is required")
    private String password;

    @NotBlank(message = "name is required")
    private String name;

    private String address;

    private Double creditScore;

    private String employmentStatus;

    private Double income;

    @NotBlank(message = "phone is required")
    @Positive
    private String phone;

    @NotBlank(message = "email is required")
    @Email
    private String email;
}
