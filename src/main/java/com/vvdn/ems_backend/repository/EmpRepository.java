package com.vvdn.ems_backend.repository;


import com.vvdn.ems_backend.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface EmpRepository extends JpaRepository<Employee, UUID> {

    List<Employee> findByIsActiveTrue();

    long count();

    long countByIsActiveTrue();

    long countByIsActiveFalse();

    long countByCreatedOnBetween(Instant start, Instant end);

    long countByCreatedOnBefore(Instant date);

    Optional<Employee> findByEmail(String email);

}
