package com.vvdn.ems_backend.repository;

import com.vvdn.ems_backend.entity.EmployeeResignation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface EmployeeResignationRepository
        extends JpaRepository<EmployeeResignation, UUID> {
}
