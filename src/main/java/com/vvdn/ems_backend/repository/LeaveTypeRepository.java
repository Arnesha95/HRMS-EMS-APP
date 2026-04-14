package com.vvdn.ems_backend.repository;

import com.vvdn.ems_backend.entity.LeaveType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface LeaveTypeRepository extends JpaRepository<LeaveType, UUID> {

}
