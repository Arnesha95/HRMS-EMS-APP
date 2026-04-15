package com.vvdn.ems_backend.dtos;

import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class MonthlyAttendanceDto {

    private UUID empId;
    private int year;
    private int month;

    private int totalDays;
    private int presentDays;
    private int absentDays;
    private int leaveDays;
    private int holidays;
    private int halfDays;
}
