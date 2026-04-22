package com.vvdn.ems_backend.dtos;

import lombok.Data;

import java.util.UUID;

@Data
public class CancelLeaveRequestDto {
    private UUID leaveApplicationId;
    private String remarks;
}
