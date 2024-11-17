package com.loan.entity;

import lombok.Getter;

@Getter
public enum ApprovalStatus {

    PENDING("PENDING"),
    APPROVED("APPROVED"),
    REJECTED("REJECTED");

    private final String description;

    ApprovalStatus(String description) {
        this.description = description;
    }

    public static ApprovalStatus findByName(String approvalStatusName) {
        for (ApprovalStatus approvalStatus : values()) {
            if (approvalStatus.description.equalsIgnoreCase(approvalStatusName)) {
                return approvalStatus;
            }
        }
        return null;
    }
}
