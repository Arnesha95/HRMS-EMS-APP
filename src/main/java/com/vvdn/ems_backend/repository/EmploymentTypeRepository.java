package com.vvdn.ems_backend.repository;

import com.vvdn.ems_backend.entity.EmploymentType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface EmploymentTypeRepository extends JpaRepository<EmploymentType, UUID> {

    Optional<EmploymentType> findByName(String name);
}

