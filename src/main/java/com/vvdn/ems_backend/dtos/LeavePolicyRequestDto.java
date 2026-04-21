package com.vvdn.ems_backend.dtos;

import lombok.Data;

import java.time.LocalDate;
import java.util.UUID;

@Data
public class LeavePolicyRequestDto {

    private UUID leaveTypeId;
    private Short noOfDays;
    private Short year;
    private LocalDate startDate;
    private LocalDate endDate;
    private UUID employeeTypeId;
    private UUID createdBy;
    private UUID updatedBy;
}