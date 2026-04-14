package com.vvdn.ems_backend.services.impl;

import com.vvdn.ems_backend.dtos.*;
import com.vvdn.ems_backend.entity.LeaveType;
import com.vvdn.ems_backend.repository.LeaveTypeRepository;
import com.vvdn.ems_backend.services.LeaveTypeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class LeaveTypeServiceImpl implements LeaveTypeService {

    private final LeaveTypeRepository repository;

    @Override
    public LeaveTypeResponseDto createLeaveType(LeaveTypeRequestDto request){

        LeaveType leaveType = LeaveType.builder()
                .type(request.getType())
                .carryForwardAllowed(request.getCarryForwardAllowed())
                .postApplicationAllowed(request.getPostApplicationAllowed())
                .maxConsecutiveDays(request.getMaxConsecutiveDays())
                .createdBy(request.getCreatedBy())
                .createdOn(Instant.now())
                .build();

        repository.save(leaveType);

        return LeaveTypeResponseDto.builder()
                .message("Leave Type created successfully")
                .build();
    }

    @Override
    public LeaveType getLeaveType(UUID id) {

        return repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Leave Type not found"));
    }

    @Override
    public List<LeaveType> getAllLeaveTypes() {

        return repository.findAll();
    }

    @Override
    public LeaveTypeResponseDto updateLeaveType(UUID id, LeaveTypeRequestDto request) {

        LeaveType leaveType = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Leave Type not found"));

        if (request.getType() != null) {
            leaveType.setType(request.getType());
        }

        if (request.getCarryForwardAllowed() != null) {
            leaveType.setCarryForwardAllowed(request.getCarryForwardAllowed());
        }

        if (request.getMaxConsecutiveDays() != null) {
            leaveType.setMaxConsecutiveDays(request.getMaxConsecutiveDays());
        }

        if (request.getCreatedBy() != null) {
            leaveType.setCreatedBy(request.getCreatedBy());
        }

        if (request.getUpdatedBy() != null) {
            leaveType.setUpdatedBy(request.getUpdatedBy());
        }

        repository.save(leaveType);

        return LeaveTypeResponseDto.builder()
                .message("Leave Type updated successfully")
                .build();
    }


    @Override
    public LeaveTypeResponseDto deleteLeaveType(UUID id) {

        LeaveType leaveType = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Leave Type not found"));

        repository.delete(leaveType);

        return LeaveTypeResponseDto.builder()
                .message("Leave Type deleted successfully")
                .build();
    }

}
