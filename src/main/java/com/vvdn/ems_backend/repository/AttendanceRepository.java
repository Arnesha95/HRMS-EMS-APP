package com.vvdn.ems_backend.repository;

import com.vvdn.ems_backend.entity.EmployeeAttendance;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface AttendanceRepository extends JpaRepository<EmployeeAttendance, UUID> {

    Optional<EmployeeAttendance> findByEmpIdAndDate(UUID empId, LocalDate date);

    List<EmployeeAttendance> findByDate(LocalDate date);

    List<EmployeeAttendance> findByEmpId(UUID empId);

    List<EmployeeAttendance> findByEmpIdAndDateBetween(
            UUID empId,
            LocalDate startDate,
            LocalDate endDate
    );

    Page<EmployeeAttendance> findByEmpIdAndDateBetween(
            UUID empId,
            LocalDate startDate,
            LocalDate endDate,
            Pageable pageable
    );
}
