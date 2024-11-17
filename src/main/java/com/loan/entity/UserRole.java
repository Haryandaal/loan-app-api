package com.loan.entity;

import lombok.Getter;

@Getter
public enum UserRole {

    ROLE_ADMIN("Admin"),
    ROLE_CUSTOMER("Customer");

    private final String description;

    UserRole(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public static UserRole findByName(String role) {
        for (UserRole userRole : values()) {
            if (userRole.getDescription().equalsIgnoreCase(role)) {
                return userRole;
            }
        }
        return null;
    }
}
