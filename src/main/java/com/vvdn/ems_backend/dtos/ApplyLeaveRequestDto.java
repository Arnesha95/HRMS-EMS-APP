package com.vvdn.ems_backend.dtos;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.UUID;

@Data
public class ApplyLeaveRequestDto {

    private UUID userId;
    private UUID empLeaveId;
    private String leaveDay;
    private String description;
    private Short noOfDays;
    private LocalDate startDate;
    private LocalDate endDate;
}
