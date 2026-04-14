package com.vvdn.ems_backend.dtos;

import lombok.Data;

import java.util.UUID;

@Data
public class LeaveApprovalRequestDto {

    private UUID hrId;
    private UUID leaveApplicationId;
    private String status; // APPROVED / REJECTED
    private String remarks;
}
