package com.vvdn.ems_backend.services;


import com.vvdn.ems_backend.dtos.AttendanceResponseDto;
import com.vvdn.ems_backend.dtos.DailyAttendanceDto;
import com.vvdn.ems_backend.dtos.EmployeeAttendanceSummaryDto;
import com.vvdn.ems_backend.dtos.MonthlyAttendanceDto;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public interface AttendanceService {

    AttendanceResponseDto markAttendance(UUID empId);

    List<AttendanceResponseDto> getTodayAttendance();

    List<AttendanceResponseDto> getAttendanceByDate(LocalDate date);

    List<AttendanceResponseDto> getAttendanceByEmpId(UUID empId);

    DailyAttendanceDto getDailyAttendance(UUID empId, LocalDate date);

    MonthlyAttendanceDto getMonthlyAttendance(UUID empId, int year, int month);

    List<DailyAttendanceDto> getAttendanceHistory(
            UUID empId,
            LocalDate startDate,
            LocalDate endDate
    );

    List<EmployeeAttendanceSummaryDto> getAllEmployeesAttendance(LocalDate date);


}
