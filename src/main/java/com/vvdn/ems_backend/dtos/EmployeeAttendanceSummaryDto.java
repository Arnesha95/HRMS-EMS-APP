package com.vvdn.ems_backend.dtos;

import com.vvdn.ems_backend.entity.AttendanceStatus;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

@Data
@Builder
public class EmployeeAttendanceSummaryDto {

    private UUID empId;
    private String fullName;

    private LocalDate date;

    private LocalTime inTime;
    private LocalTime outTime;
    private LocalTime workingHour;

    private AttendanceStatus status;
    private String remarks;
}
