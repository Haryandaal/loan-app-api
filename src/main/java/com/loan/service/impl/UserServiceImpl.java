package com.loan.service.impl;

import com.loan.dto.UserRequest;
import com.loan.dto.UserResponse;
import com.loan.dto.UserUpdatePasswordRequest;
import com.loan.entity.Role;
import com.loan.entity.UserAccount;
import com.loan.repository.RoleRepository;
import com.loan.repository.UserAccountRepository;
import com.loan.service.UserService;
import com.loan.util.ValidationUtil;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserAccountRepository userAccountRepository;
    private final PasswordEncoder passwordEncoder;
    private final ValidationUtil validationUtil;
    private final RoleRepository roleRepository;

    @Value("${USERNAME_ADMIN:admin}")
    private String USERNAME_ADMIN;

    @Value("${PASSWORD_ADMIN:password}")
    private String PASSWORD_ADMIN;

    @Transactional(rollbackFor = Exception.class)
    @PostConstruct
    public void initUser() {
        boolean existsByUsername = userAccountRepository.existsByUsername(USERNAME_ADMIN);
        if (existsByUsername) return;

        Role userRole = roleRepository.findByName("Admin")
                .orElseGet(() -> {
                    // Jika role tidak ditemukan, buat dan simpan role Admin baru
                    Role newRole = Role.builder()
                            .name("Admin")
                            .build();
                    return roleRepository.save(newRole);
                });

        UserAccount userAccount = UserAccount.builder()
                .id(UUID.randomUUID().toString())
                .username(USERNAME_ADMIN)
                .password(passwordEncoder.encode(PASSWORD_ADMIN))
                .role(userRole)
                .build();
        userAccountRepository.save(
                userAccount.getId(),
                userAccount.getUsername(),
                userAccount.getPassword(),
                userAccount.getRole().getId()
        );
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public UserResponse create(UserRequest userRequest) {
        validationUtil.validate(userRequest);

        if (userAccountRepository.findByUsername(userRequest.getUsername()).isPresent())
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Username already exists");

        Role userRole = roleRepository.findByName(userRequest.getRole())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid role"));

        UserAccount userAccount = UserAccount.builder()
                .id(UUID.randomUUID().toString())
                .username(userRequest.getUsername())
                .password(passwordEncoder.encode(userRequest.getPassword()))
                .role(userRole)
                .build();
        userAccountRepository.save(
                userAccount.getId(),
                userAccount.getUsername(),
                userAccount.getPassword(),
                userAccount.getRole().getId()
        );
        return toUserResponse(userAccount);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public UserAccount create(UserAccount userAccount) {
        userAccount.setPassword(passwordEncoder.encode(userAccount.getPassword()));
        userAccountRepository.save(
                userAccount.getId(),
                userAccount.getUsername(),
                userAccount.getPassword(),
                userAccount.getRole().getId()
        );
        return userAccount;
    }

    @Override
    public UserAccount getById(String id) {
        return userAccountRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
    }

    @Transactional(readOnly = true)
    @Override
    public UserResponse getAuthentication() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserAccount userAccount = (UserAccount) authentication.getPrincipal();
        loadUserByUsername(userAccount.getUsername());
        return toUserResponse(userAccount);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void updatePassword(String id, UserUpdatePasswordRequest request) {
        validationUtil.validate(request);
        UserAccount userAccount = getById(id);

        if (!passwordEncoder.matches(userAccount.getPassword(), request.getCurrentPassword())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Password does not match");
        }

        userAccount.setPassword(passwordEncoder.encode(request.getNewPassword()));
        userAccountRepository.save(
                userAccount.getId(),
                userAccount.getUsername(),
                userAccount.getPassword(),
                userAccount.getRole().getId()
        );
    }

    @Transactional(readOnly = true)
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userAccountRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Username not found"));
    }

    private UserResponse toUserResponse(UserAccount userAccount) {
        return UserResponse.builder()
                .id(userAccount.getId())
                .username(userAccount.getUsername())
                .role(userAccount.getRole().getName())
                .build();
    }
}
