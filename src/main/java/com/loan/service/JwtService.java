package com.loan.service;

import com.loan.entity.UserAccount;
import jakarta.servlet.http.HttpServletRequest;

public interface JwtService {
    String generateAccessToken(UserAccount userAccount);
    boolean validateToken(String token);
    String getUserId(String token);
    String extractTokenFromRequest(HttpServletRequest request);
    void blacklistAccessToken(String bearerToken);
    boolean isTokenBlacklisted(String token);
}
