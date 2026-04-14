package com.vvdn.ems_backend.services;

import com.vvdn.ems_backend.dtos.AttendancePolicyRequestDto;
import com.vvdn.ems_backend.dtos.AttendancePolicyResponseDto;
import com.vvdn.ems_backend.entity.AttendancePolicy;

import java.util.List;
import java.util.UUID;

public interface AttendancePolicyService {

    AttendancePolicyResponseDto createPolicy(AttendancePolicyRequestDto request);

    AttendancePolicyResponseDto updatePolicy(UUID id, AttendancePolicyRequestDto request);

    AttendancePolicyResponseDto deactivatedAttendancePolicy(UUID id);

    AttendancePolicy getPolicyById(UUID id);

    List<AttendancePolicy> getAllPolicies();
}
