package com.vvdn.ems_backend.dtos;

import lombok.Builder;
import lombok.Data;

import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;

@Data
@Builder
public class LeaveDetailsResponseDto {

    private UUID leaveApplicationId;
    private String employeeName;
    private String leaveType;

    private Short noOfDays;
    private LocalDate startDate;
    private LocalDate endDate;

    private String status;

    private Instant createdOn;

    private LocalDate approvedOn;
    private LocalDate rejectedOn;

    private UUID approvedBy;
    private UUID rejectedBy;

    private String approver;
    private String denier;
    private String remarks;
}
