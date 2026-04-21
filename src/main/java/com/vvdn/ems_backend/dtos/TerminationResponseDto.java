package com.vvdn.ems_backend.dtos;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.util.UUID;

@Data
@Builder
public class TerminationResponseDto {

    private UUID offboardingId;
    private UUID empId;
    private String employeeName;
    private LocalDate terminationDate;
    private String reason;
    private String status;
    private Boolean isGoodToRehire;
}
