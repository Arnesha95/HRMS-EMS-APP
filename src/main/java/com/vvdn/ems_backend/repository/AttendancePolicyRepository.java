package com.vvdn.ems_backend.repository;

import com.vvdn.ems_backend.entity.AttendancePolicy;
import org.springframework.data.jpa.repository.JpaRepository;


import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface AttendancePolicyRepository extends JpaRepository<AttendancePolicy, UUID> {

    //Optional<AttendancePolicy> findByIsActiveTrue();

    Optional<AttendancePolicy> findTopByIsActiveTrueOrderByCreatedOnDesc();
}
