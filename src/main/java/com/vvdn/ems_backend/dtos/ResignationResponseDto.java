package com.vvdn.ems_backend.dtos;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.util.UUID;

@Data
@Builder
public class ResignationResponseDto {

    private UUID offboardingId;
    private UUID empId;
    private String employeeName;
    private LocalDate resignationDate;
    private LocalDate proposedLastWorkingDate;
    private LocalDate finalLastWorkingDate;
    private String status;
}
