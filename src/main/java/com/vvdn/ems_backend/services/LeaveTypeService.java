package com.vvdn.ems_backend.services;

import com.vvdn.ems_backend.dtos.LeaveTypeRequestDto;
import com.vvdn.ems_backend.dtos.LeaveTypeResponseDto;
import com.vvdn.ems_backend.entity.LeaveType;

import java.util.List;
import java.util.UUID;

public interface LeaveTypeService {

    LeaveTypeResponseDto createLeaveType(LeaveTypeRequestDto dto);

    LeaveType getLeaveType(UUID id);

    List<LeaveType> getAllLeaveTypes();

    LeaveTypeResponseDto updateLeaveType(UUID id, LeaveTypeRequestDto dto);

   // LeaveTypeResponseDto deactivateLeaveType(UUID id, LeaveTypeResponseDto dto);

    LeaveTypeResponseDto deleteLeaveType(UUID id);
}
