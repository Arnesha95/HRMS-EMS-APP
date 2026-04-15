package com.vvdn.ems_backend.repository;

import com.vvdn.ems_backend.entity.Employee;
import com.vvdn.ems_backend.entity.EmployeeLeave;
import com.vvdn.ems_backend.entity.LeavePolicy;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface EmployeeLeaveRepository extends JpaRepository<EmployeeLeave, UUID> {

    boolean existsByEmployeeAndLeavePolicy(Employee employee, LeavePolicy leavePolicy);

    List<EmployeeLeave> findByEmployee_EmpId(UUID empId);

    Optional<EmployeeLeave> findByEmployee_EmpIdAndLeavePolicy_PolicyId(UUID empId, UUID policyId);

    Optional<EmployeeLeave> findTopByEmployee_EmpIdAndLeavePolicy_LeaveType_TypeIdOrderByCreatedOnDesc(
            UUID empId, UUID leaveTypeId);


}