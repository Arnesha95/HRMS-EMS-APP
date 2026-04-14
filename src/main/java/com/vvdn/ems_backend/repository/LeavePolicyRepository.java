package com.vvdn.ems_backend.repository;


import com.vvdn.ems_backend.entity.EmployeeLeave;
import com.vvdn.ems_backend.entity.LeavePolicy;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface LeavePolicyRepository extends JpaRepository<LeavePolicy, UUID> {

    List<LeavePolicy> findByEmploymentType_Id(UUID employmentTypeId);

    List<LeavePolicy> findByYear(Integer year);

    Optional<LeavePolicy> findByYearAndLeaveType_TypeId(Integer year, UUID typeId);

}
