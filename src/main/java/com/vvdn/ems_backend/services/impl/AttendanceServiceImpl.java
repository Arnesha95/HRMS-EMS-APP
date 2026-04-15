package com.vvdn.ems_backend.services.impl;

import com.vvdn.ems_backend.dtos.AttendanceResponseDto;
import com.vvdn.ems_backend.dtos.DailyAttendanceDto;
import com.vvdn.ems_backend.dtos.MonthlyAttendanceDto;
import com.vvdn.ems_backend.entity.AttendancePolicy;
import com.vvdn.ems_backend.entity.AttendanceStatus;
import com.vvdn.ems_backend.entity.EmployeeAttendance;
import com.vvdn.ems_backend.entity.HolidayCalendar;
import com.vvdn.ems_backend.exception.AttendanceNotFoundException;
import com.vvdn.ems_backend.repository.AttendanceRepository;
import com.vvdn.ems_backend.repository.AttendancePolicyRepository;
import com.vvdn.ems_backend.repository.HolidayRepository;
import com.vvdn.ems_backend.repository.LeaveRepository;
import com.vvdn.ems_backend.services.AttendanceService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.*;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AttendanceServiceImpl implements AttendanceService {

    private final HolidayRepository holidayRepository;
    private final LeaveRepository leaveRepository;
    private final AttendanceRepository attendanceRepository;
    private final AttendancePolicyRepository attendancePolicyRepository;

    @Override
    public AttendanceResponseDto markAttendance(UUID empId) {

        LocalDate today = LocalDate.now();
        LocalTime now = LocalTime.now();

        EmployeeAttendance attendance = attendanceRepository
                .findByEmpIdAndDate(empId, today)
                .orElse(null);

        if (attendance == null) {
            attendance = EmployeeAttendance.builder()
                    .empId(empId)
                    .date(today)
                    .inTime(now)
                    .createdOn(Instant.now())
                    .build();

            attendanceRepository.save(attendance);

            return mapToDto(attendance, "Check-in recorded");
        }

        attendance.setOutTime(now);

        if (attendance.getInTime() != null) {
            Duration duration = Duration.between(attendance.getInTime(), now);
            attendance.setWorkingHour(
                    LocalTime.ofSecondOfDay(duration.getSeconds())
            );
        }

        attendance.setUpdatedOn(Instant.now());

        attendanceRepository.save(attendance);

        return mapToDto(attendance, "Check-out updated");
    }

    @Override
    public List<AttendanceResponseDto> getTodayAttendance() {
        return getAttendanceByDate(LocalDate.now());
    }

    @Override
    public List<AttendanceResponseDto> getAttendanceByDate(LocalDate date) {

        List<EmployeeAttendance> list = attendanceRepository.findByDate(date);

        return list.stream()
                .map(a -> mapToDto(a, "Fetched"))
                .collect(Collectors.toList());
    }

    private AttendanceResponseDto mapToDto(EmployeeAttendance a, String msg) {
        return AttendanceResponseDto.builder()
                .empId(a.getEmpId())
                .date(a.getDate())
                .inTime(a.getInTime())
                .outTime(a.getOutTime())
                .workingHour(a.getWorkingHour())
                .message(msg)
                .build();
    }

    @Override
    public List<AttendanceResponseDto> getAttendanceByEmpId(UUID empId) {

        List<EmployeeAttendance> list = attendanceRepository.findByEmpId(empId);


        if (list.isEmpty()) {
            throw new AttendanceNotFoundException("No attendance found for employee");
        }

        return list.stream()
                .map(a -> mapToDto(a, "Fetched"))
                .collect(Collectors.toList());
    }

    @Override
    public DailyAttendanceDto getDailyAttendance(UUID empId, LocalDate date) {

        // 1. Holiday
        Optional<HolidayCalendar> holiday =
                holidayRepository.findByHolidayDateAndIsActiveTrue(date);

        if (holiday.isPresent()) {
            return buildResponse(empId, date, AttendanceStatus.HOLIDAY, "Holiday");
        }

        // 2. Leave
        boolean onLeave = leaveRepository
                .existsByEmployeeLeaves_Employee_EmpIdAndStartDateLessThanEqualAndEndDateGreaterThanEqual(
                        empId, date, date);

        if (onLeave) {
            return buildResponse(empId, date, AttendanceStatus.LEAVE, "On Leave");
        }

        // 3. Attendance
        Optional<EmployeeAttendance> attendance =
                attendanceRepository.findByEmpIdAndDate(empId, date);

        if (attendance.isPresent()) {

            EmployeeAttendance att = attendance.get();
            AttendancePolicy policy = getActivePolicy();

            if (att.getWorkingHour() != null &&
                    att.getWorkingHour().getHour() >= policy.getMinWorkingHour()) {

                return buildResponseFromAttendance(att, AttendanceStatus.PRESENT);

            } else if (att.getWorkingHour() != null &&
                    att.getWorkingHour().getHour() >= policy.getHalfDayHour()) {

                return buildResponseFromAttendance(att, AttendanceStatus.HALF_DAY);
            }

            return buildResponseFromAttendance(att, AttendanceStatus.ABSENT);
        }

        // 4. Default
        return buildResponse(empId, date, AttendanceStatus.ABSENT, "No record");
    }

    private AttendancePolicy getActivePolicy() {
        return attendancePolicyRepository.findByIsActiveTrue()
                .orElseThrow(() -> new RuntimeException("No active policy"));
    }

    private DailyAttendanceDto buildResponse(UUID empId, LocalDate date,
                                             AttendanceStatus status, String remark) {
        return DailyAttendanceDto.builder()
                .empId(empId)
                .date(date)
                .status(status)
                .remarks(remark)
                .build();
    }

    private DailyAttendanceDto buildResponseFromAttendance(EmployeeAttendance att,
                                                           AttendanceStatus status) {
        return DailyAttendanceDto.builder()
                .empId(att.getEmpId())
                .date(att.getDate())
                .inTime(att.getInTime())
                .outTime(att.getOutTime())
                .workingHour(att.getWorkingHour())
                .status(status)
                .remarks("Calculated from attendance")
                .build();
    }

    @Override
    public MonthlyAttendanceDto getMonthlyAttendance(UUID empId, int year, int month) {
        LocalDate start = LocalDate.of(year, month, 1);
        LocalDate end = start.withDayOfMonth(start.lengthOfMonth());

        int present = 0, absent = 0, leave = 0, holiday = 0, halfDay = 0;

        for (LocalDate date = start; !date.isAfter(end); date = date.plusDays(1)) {

            DailyAttendanceDto daily = getDailyAttendance(empId, date);

            switch (daily.getStatus()) {
                case PRESENT -> present++;
                case ABSENT -> absent++;
                case LEAVE -> leave++;
                case HOLIDAY -> holiday++;
                case HALF_DAY -> halfDay++;
            }
        }

        return MonthlyAttendanceDto.builder()
                .empId(empId)
                .year(year)
                .month(month)
                .totalDays(start.lengthOfMonth())
                .presentDays(present)
                .absentDays(absent)
                .leaveDays(leave)
                .holidays(holiday)
                .halfDays(halfDay)
                .build();
    }
}
