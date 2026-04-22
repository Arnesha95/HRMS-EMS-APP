package com.vvdn.ems_backend.controllers;

import com.vvdn.ems_backend.dtos.*;
import com.vvdn.ems_backend.services.AttendanceService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/attendance")
@RequiredArgsConstructor
public class AttendanceController {

    private final AttendanceService attendanceService;



    @PostMapping()
    public AttendanceResponseDto markAttendance(
            @RequestBody AttendanceRequestDto request) {

        return attendanceService.markAttendance(request.getEmpId());
    }


    @GetMapping("/today")
    public List<AttendanceResponseDto> getTodayAttendance() {
        return attendanceService.getTodayAttendance();

    }


    @GetMapping("/date")
    public List<AttendanceResponseDto> getAttendanceByDate(
            @RequestParam
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate date) {

        return attendanceService.getAttendanceByDate(date);
    }


    @GetMapping("/employee/{empId}")
    public List<AttendanceResponseDto> getAttendanceByEmpId(
            @PathVariable UUID empId) {

        return attendanceService.getAttendanceByEmpId(empId);
    }


    @GetMapping("/daily")
    public DailyAttendanceDto getDailyAttendance(
            @RequestParam UUID empId,
            @RequestParam
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate date) {

        return attendanceService.getDailyAttendance(empId, date);
    }


    @GetMapping("/monthly")
    public MonthlyAttendanceDto getMonthlyAttendance(
            @RequestParam UUID empId,
            @RequestParam int year,
            @RequestParam int month) {

        return attendanceService.getMonthlyAttendance(empId, year, month);
    }


    @GetMapping("/history")
    public List<DailyAttendanceDto> getAttendanceHistory(
            @RequestParam UUID empId,

            @RequestParam
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate startDate,

            @RequestParam
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate endDate) {

        return attendanceService.getAttendanceHistory(empId, startDate, endDate);

    }

    @GetMapping("/all")
    public List<EmployeeAttendanceSummaryDto> getAllEmployeesAttendance(
            @RequestParam
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate date) {

        return attendanceService.getAllEmployeesAttendance(date);
    }
}
