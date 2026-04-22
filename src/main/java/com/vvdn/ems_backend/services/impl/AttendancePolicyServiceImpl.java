package com.vvdn.ems_backend.services.impl;

import com.vvdn.ems_backend.dtos.*;
import com.vvdn.ems_backend.entity.AttendancePolicy;
import com.vvdn.ems_backend.repository.AttendancePolicyRepository;
import com.vvdn.ems_backend.services.AttendancePolicyService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AttendancePolicyServiceImpl implements AttendancePolicyService {

    private final AttendancePolicyRepository repository;


    @Override
    public AttendancePolicyResponseDto createPolicy(AttendancePolicyRequestDto request) {

        AttendancePolicy attendancePolicy = AttendancePolicy.builder()
                .minInTime(request.getMinInTime())
                .minOutTime(request.getMinOutTime())
                .minWorkingHour(request.getMinWorkingHour())
                .halfDayHour(request.getHalfDayHour())
                .isActive(true)
                .createdBy(request.getCreatedBy())
                .updatedBy(request.getUpdatedBy())
                .build();

        repository.save(attendancePolicy);

        return AttendancePolicyResponseDto.builder()
                .message("Attendance Policy created successfully")
                .build();
    }

    @Override
    public AttendancePolicyResponseDto updatePolicy(UUID id, AttendancePolicyRequestDto request) {

        AttendancePolicy attendancePolicy = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Attendance Policy not found"));


        if (request.getMinOutTime() != null)
            attendancePolicy.setMinOutTime(request.getMinOutTime());

        if (request.getMinInTime() != null)
            attendancePolicy.setMinInTime(request.getMinInTime());

        if (request.getMinWorkingHour() != null)
            attendancePolicy.setMinWorkingHour(request.getMinWorkingHour());

        if (request.getHalfDayHour() != null)
            attendancePolicy.setHalfDayHour(request.getHalfDayHour());

//        if (request.getCreatedBy() != null)
//            attendancePolicy.setCreatedBy(request.getCreatedBy());

        if (request.getUpdatedBy() != null)
            attendancePolicy.setUpdatedBy(request.getUpdatedBy());

        repository.save(attendancePolicy);

        return AttendancePolicyResponseDto.builder()
                .message("Attendance Policy updated successfully")
                .build();
    }

    @Override
    public AttendancePolicyResponseDto deactivatedAttendancePolicy(UUID id) {

        AttendancePolicy attendancePolicy = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Attendance Policy not found"));

        attendancePolicy.setIsActive(false);
        attendancePolicy.setUpdatedOn(Instant.now());

        repository.save(attendancePolicy);

        return AttendancePolicyResponseDto.builder()
                .message("Attendance Policy deactivated successfully")
                .build();
    }

    @Override
    public List<AttendancePolicy> getAllPolicies() {
        return repository.findAll();
    }

    @Override
    public AttendancePolicy getPolicyById(UUID id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Attendance Policy not found"));
    }
}
