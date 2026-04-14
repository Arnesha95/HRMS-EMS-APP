package com.vvdn.ems_backend.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
public class EmployeeLeaveResponseDto {

    private UUID empLeaveId;
    private String leaveType;
    private float totalLeaves;
    private float usedLeaves;
    private float remainingLeaves;
    private int year;

}
