package com.vvdn.ems_backend.dtos;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

@Data
@Builder
public class AttendanceResponseDto {

    private UUID empId;
    private LocalDate date;
    private LocalTime inTime;
    private LocalTime outTime;
    private LocalTime workingHour;
    private String message;
}
