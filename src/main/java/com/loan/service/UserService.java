package com.loan.service;

import com.loan.dto.UserRequest;
import com.loan.dto.UserResponse;
import com.loan.dto.UserUpdatePasswordRequest;
import com.loan.entity.UserAccount;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService extends UserDetailsService {

    UserResponse create(UserRequest userRequest);

    UserAccount create(UserAccount userAccount);

    UserAccount getById(String id);

    UserResponse getAuthentication();

    void updatePassword(String id, UserUpdatePasswordRequest request);
}
