package com.vvdn.ems_backend.dtos;

import lombok.Data;

import java.time.LocalDate;
import java.util.UUID;

@Data
public class EmployeeLeaveRequestDto {

    private UUID empId;
    private UUID policyId;

    private LocalDate joiningDate;

    private Short year;

    private UUID createdBy;
}
