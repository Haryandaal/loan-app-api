package com.loan.entity;

import lombok.Getter;

@Getter
public enum EmploymentStatus {

    FULL_TIME("FULL_TIME"),
    PART_TIME("PART_TIME"),
    CONTRACT("CONTRACT"),
    UNEMPLOYED("UNEMPLOYED");

    private final String description;

    EmploymentStatus(String description) {
        this.description = description;
    }

    public static EmploymentStatus findByName(String approvalStatusName) {
        for (EmploymentStatus employmentStatus : values()) {
            if (employmentStatus.description.equalsIgnoreCase(approvalStatusName)) {
                return employmentStatus;
            }
        }
        return null;
    }
}
