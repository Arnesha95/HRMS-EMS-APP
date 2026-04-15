package com.vvdn.ems_backend.services;

import com.vvdn.ems_backend.dtos.EmployeeLeaveRequestDto;
import com.vvdn.ems_backend.dtos.EmployeeLeaveResponseDto;
import com.vvdn.ems_backend.entity.EmployeeLeave;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public interface EmployeeLeaveService {

    List<EmployeeLeaveResponseDto> allocateLeaves(EmployeeLeaveRequestDto request);

    List<EmployeeLeaveResponseDto> getEmployeeLeaves(UUID empId);

}
