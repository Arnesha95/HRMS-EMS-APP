package com.vvdn.ems_backend.services;

import com.vvdn.ems_backend.dtos.LeavePolicyRequestDto;
import com.vvdn.ems_backend.dtos.LeavePolicyResponseDto;
import com.vvdn.ems_backend.entity.LeavePolicy;

import java.util.List;
import java.util.UUID;

public interface LeavePolicyService {

    LeavePolicyResponseDto createPolicy(LeavePolicyRequestDto request);

    LeavePolicy getPolicyById(UUID policyId);

    List<LeavePolicy> getAllPolicies();

    LeavePolicyResponseDto updatePolicy(UUID policyId, LeavePolicyRequestDto request);

    LeavePolicyResponseDto deletePolicy(UUID policyId);
}
