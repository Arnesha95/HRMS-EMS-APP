package com.vvdn.ems_backend.repository;

import com.vvdn.ems_backend.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface EmpRepository extends JpaRepository<Employee, UUID> {

    List<Employee> findByIsActiveTrue();

}
