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
    private Float totalLeaves;
    private Float usedLeaves;
    private Float remainingLeaves;
    private Short year;

}
