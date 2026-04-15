package com.vvdn.ems_backend.repository;

import com.vvdn.ems_backend.entity.EmployeeOffboarding;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface EmployeeOffboardingRepository
        extends JpaRepository<EmployeeOffboarding, UUID> {
}
