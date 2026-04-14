package com.vvdn.ems_backend.repository;

import com.vvdn.ems_backend.entity.Designation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface DesignationRepository extends JpaRepository<Designation, UUID> {

    List<Designation>findByIsActiveTrue();
}
