package com.vvdn.ems_backend.repository;

import com.vvdn.ems_backend.dtos.TerminationRequestDto;
import com.vvdn.ems_backend.dtos.TerminationResponseDto;
import com.vvdn.ems_backend.entity.EmployeeOffboarding;
import com.vvdn.ems_backend.entity.OffboardingStatus;
import com.vvdn.ems_backend.entity.OffboardingType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;


public interface EmployeeOffboardingRepository extends JpaRepository<EmployeeOffboarding, UUID> {

    List<EmployeeOffboarding> findByOffboardingType(OffboardingType offboardingType);


    List<EmployeeOffboarding> findByOffboardingStatus(OffboardingStatus status);


    List<EmployeeOffboarding> findByOffboardingStatusAndOffboardingType(
            OffboardingStatus status,
            OffboardingType type
    );


    boolean existsByEmployeeEmpIdAndOffboardingStatus(UUID empId, OffboardingStatus status);

    boolean existsByEmployeeEmpIdAndOffboardingType(UUID empId, OffboardingType offboardingType);

}
