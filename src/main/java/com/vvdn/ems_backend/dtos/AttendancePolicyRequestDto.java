package com.vvdn.ems_backend.dtos;

import lombok.Data;

import java.time.Instant;
import java.time.LocalTime;
import java.util.UUID;

@Data
public class AttendancePolicyRequestDto {

    private LocalTime minInTime;
    private LocalTime minOutTime;
    private Short minWorkingHour;
    private Short halfDayHour;
    private Boolean isActive;
    private UUID createdBy;
    private UUID updatedBy;
    private Instant createdOn;
    private Instant updatedOn;
}
