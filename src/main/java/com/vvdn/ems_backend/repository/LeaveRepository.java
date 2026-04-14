package com.vvdn.ems_backend.repository;

import com.vvdn.ems_backend.entity.LeaveApplication;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface LeaveRepository extends JpaRepository<LeaveApplication, UUID> {


}
