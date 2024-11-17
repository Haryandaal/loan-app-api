package com.loan.controller;

import com.loan.dto.*;
import com.loan.service.RoleService;
import com.loan.util.ResponseUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "api/roles")
public class RoleController {

    private final RoleService roleService;


    @PostMapping
    public ResponseEntity<WebResponse<RoleResponse>> createRole(@RequestBody RoleRequest request) {
        RoleResponse response = roleService.create(request);
        return ResponseUtil.buildResponse(HttpStatus.CREATED, "Role created", response);
    }

    @GetMapping
    public ResponseEntity<WebResponse<List<RoleResponse>>> getAllRoles() {
        List<RoleResponse> responses = roleService.getAll();
        return ResponseUtil.buildResponse(HttpStatus.OK, "Roles found", responses);
    }

    @DeleteMapping(path = "{id}")
    public ResponseEntity<WebResponse<String>> deleteRoleById(@PathVariable(name = "id") String id) {
        roleService.deleteById(id);
        return ResponseUtil.buildResponse(HttpStatus.OK, "Role deleted", null);
    }
}
