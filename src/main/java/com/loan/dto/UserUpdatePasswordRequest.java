package com.loan.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserUpdatePasswordRequest {

    private String currentPassword;

    private String newPassword;
}
