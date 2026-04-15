package com.vvdn.ems_backend.dtos;

import com.vvdn.ems_backend.entity.AttendanceStatus;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

@Data
@Builder
public class DailyAttendanceDto {

    private UUID empId;
    private LocalDate date;
    private AttendanceStatus status;

    private LocalTime inTime;
    private LocalTime outTime;
    private LocalTime workingHour;

    private String remarks; // Optional (Late, Half-day, etc.)
}
