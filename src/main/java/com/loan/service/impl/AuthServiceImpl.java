package com.loan.service.impl;

import com.loan.dto.AuthRequest;
import com.loan.dto.AuthResponse;
import com.loan.entity.UserAccount;
import com.loan.service.AuthService;
import com.loan.service.JwtService;
import com.loan.service.UserService;
import com.loan.util.ValidationUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final ValidationUtil validationUtil;
    private final UserService userService;


    @Override
    public AuthResponse login(AuthRequest request) {
        validationUtil.validate(request);
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        UserAccount userAccount = (UserAccount) authentication.getPrincipal();
        String accessToken = jwtService.generateAccessToken(userAccount);

        return AuthResponse.builder()
                .accessToken(accessToken)
                .role(userAccount.getUserRole().getName())
                .build();
    }

    @Override
    public void logout(String accessToken) {
        UserAccount userAccount = (UserAccount) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        jwtService.blacklistAccessToken(accessToken);
    }
}
