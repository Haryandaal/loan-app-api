package com.loan.service;

import com.loan.dto.RoleRequest;
import com.loan.dto.RoleResponse;
import com.loan.entity.Role;

import java.util.List;

public interface RoleService {

    RoleResponse create(RoleRequest request);

    List<RoleResponse> getAll();

    Role getById(String id);

    RoleResponse getOne(String id);

    RoleResponse update(String id, RoleRequest request);

    void deleteById(String id);
}
