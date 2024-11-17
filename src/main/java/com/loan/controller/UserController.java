package com.loan.controller;

import com.loan.dto.UserResponse;
import com.loan.dto.UserUpdatePasswordRequest;
import com.loan.service.UserService;
import com.loan.util.ResponseUtil;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "api/users")
@RequiredArgsConstructor
@SecurityRequirement(name = "Bearer Authentication")
public class UserController {

    private final UserService userService;

    @GetMapping(path = "me")
    public ResponseEntity<?> getSelfInfo() {
        UserResponse userResponse = userService.getAuthentication();
        return ResponseUtil.buildResponse(HttpStatus.OK, "Success fetch user info", userResponse);
    }

    @PatchMapping("/{id}/update-password")
    public ResponseEntity<?> updatePassword(@PathVariable String id, @RequestBody UserUpdatePasswordRequest request) {
        userService.updatePassword(id, request);
        return ResponseUtil.buildResponse(HttpStatus.OK, "Password updated", null);
    }
}
