package com.vvdn.ems_backend.dtos;

import lombok.Builder;
import lombok.Data;

import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;

@Data
@Builder
public class LeaveHistoryResponseDto {

    private UUID leaveApplicationId;
    private String employeeName;
    private Instant createdOn;
    private String leaveType;
    private Short noOfDays;
    private LocalDate startDate;
    private LocalDate endDate;
    private String status;
    private String remarks;
}
