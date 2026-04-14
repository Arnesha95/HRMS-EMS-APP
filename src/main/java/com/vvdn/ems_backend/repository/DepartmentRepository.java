package com.vvdn.ems_backend.repository;

import com.vvdn.ems_backend.entity.Department;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface DepartmentRepository extends JpaRepository<Department, UUID> {

    List<Department> findByIsActiveTrue();
}
