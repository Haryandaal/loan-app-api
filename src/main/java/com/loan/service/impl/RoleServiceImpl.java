package com.loan.service.impl;

import com.loan.dto.RoleRequest;
import com.loan.dto.RoleResponse;
import com.loan.entity.Role;
import com.loan.repository.RoleRepository;
import com.loan.service.RoleService;
import com.loan.util.ValidationUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;
    private final ValidationUtil validationUtil;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public RoleResponse create(RoleRequest request) {
        validationUtil.validate(request);

        if (roleRepository.existsByName(request.getName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Role already exists");
        }

        Role role = Role.builder()
                .name(request.getName())
                .build();

        Role savedRole = roleRepository.save(role);

        return RoleResponse.builder()
                .id(savedRole.getId())
                .name(savedRole.getName())
                .build();
    }

    @Transactional(readOnly = true)
    @Override
    public List<RoleResponse> getAll() {
        return roleRepository.findAll().stream()
                .map(role -> RoleResponse.builder()
                        .id(role.getId())
                        .name(role.getName())
                        .build())
                .toList();
    }

    @Transactional(readOnly = true)
    @Override
    public Role getById(String id) {
        return roleRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Role not found"));
    }

    @Transactional(readOnly = true)
    @Override
    public RoleResponse getOne(String id) {
        Role role = roleRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Role not found"));
        return RoleResponse.builder()
                .id(role.getId())
                .name(role.getName())
                .build();

    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public RoleResponse update(String id, RoleRequest request) {
        return null;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void deleteById(String id) {
        if (!roleRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Role not found");
        }
        roleRepository.deleteById(id);
    }
}