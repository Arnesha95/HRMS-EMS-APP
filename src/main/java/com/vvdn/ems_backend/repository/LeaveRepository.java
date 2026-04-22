package com.vvdn.ems_backend.repository;

import com.vvdn.ems_backend.entity.LeaveApplication;
import com.vvdn.ems_backend.entity.LeaveStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public interface LeaveRepository extends JpaRepository<LeaveApplication, UUID> {

    List<LeaveApplication> findByEmployeeLeaves_Employee_EmpIdOrderByCreatedOnDesc(UUID empId);


    boolean existsByEmployeeLeaves_Employee_EmpIdAndStatusInAndStartDateLessThanEqualAndEndDateGreaterThanEqual(
            UUID empId,
            List<LeaveStatus> statuses,
            LocalDate endDate,
            LocalDate startDate
    );


    List<LeaveApplication> findAllByOrderByCreatedOnDesc();

    List<LeaveApplication> findByStatusOrderByCreatedOnDesc(LeaveStatus status);

    long countByStatus(LeaveStatus status);

}
