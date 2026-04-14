package com.vvdn.ems_backend.repository;

import com.vvdn.ems_backend.entity.AttendancePolicy;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

public interface AttendancePolicyRepository extends JpaRepository<AttendancePolicy, UUID> {

}
