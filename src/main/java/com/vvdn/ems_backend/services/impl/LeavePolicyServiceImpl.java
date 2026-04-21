package com.vvdn.ems_backend.services.impl;

import com.vvdn.ems_backend.dtos.LeavePolicyRequestDto;
import com.vvdn.ems_backend.dtos.LeavePolicyResponseDto;
import com.vvdn.ems_backend.entity.EmploymentType;
import com.vvdn.ems_backend.entity.LeavePolicy;
import com.vvdn.ems_backend.entity.LeaveType;
import com.vvdn.ems_backend.repository.EmploymentTypeRepository;
import com.vvdn.ems_backend.repository.LeavePolicyRepository;
import com.vvdn.ems_backend.repository.LeaveTypeRepository;
import com.vvdn.ems_backend.services.LeavePolicyService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.UUID;


@Service
@RequiredArgsConstructor
public class LeavePolicyServiceImpl implements LeavePolicyService {

    private final LeavePolicyRepository leavePolicyRepository;
    private final LeaveTypeRepository leaveTypeRepository;
    private final EmploymentTypeRepository employmentTypeRepository;

    @Override
    public LeavePolicyResponseDto createPolicy(LeavePolicyRequestDto request) {

        LeaveType leaveType = leaveTypeRepository
                .findById(request.getLeaveTypeId())
                .orElseThrow(() -> new RuntimeException("Leave Type not found"));

        EmploymentType employmentType = employmentTypeRepository
                .findById(request.getEmployeeTypeId())
                .orElseThrow(() -> new RuntimeException("Employment Type not found"));

        LeavePolicy policy = LeavePolicy.builder()
                .leaveType(leaveType)
                .noOfDays(request.getNoOfDays())
                .year(request.getYear())
                .startDate(request.getStartDate())
                .endDate(request.getEndDate())
                .employmentType(employmentType)
                .createdBy(request.getCreatedBy())
                .createdOn(Instant.now())
                .updatedBy(request.getUpdatedBy())
                .updatedOn(Instant.now())
                .build();

        leavePolicyRepository.save(policy);

        return LeavePolicyResponseDto.builder()
                .message("Leave Policy created successfully")
                .build();
    }

    @Override
    public LeavePolicy getPolicyById(UUID policyId) {

        return leavePolicyRepository.findById(policyId)
                .orElseThrow(() -> new RuntimeException("Leave Policy not found"));

    }

    @Override
    public List<LeavePolicy> getAllPolicies() {

        return leavePolicyRepository.findAll();

    }


    @Override
    public LeavePolicyResponseDto updatePolicy(UUID policyId, LeavePolicyRequestDto request) {

        LeavePolicy policy = leavePolicyRepository.findById(policyId)
                .orElseThrow(() -> new RuntimeException("Policy not found"));

        if (request.getLeaveTypeId() != null) {
            LeaveType leaveType = leaveTypeRepository
                    .findById(request.getLeaveTypeId())
                    .orElseThrow(() -> new RuntimeException("Leave Type not found"));

            policy.setLeaveType(leaveType);
        }

        if (request.getNoOfDays() != null) {
            policy.setNoOfDays(request.getNoOfDays());
        }

        if (request.getStartDate() != null) {
            policy.setStartDate(request.getStartDate());
        }

        if (request.getEndDate() != null) {
            policy.setEndDate(request.getEndDate());
        }

        if (request.getEmployeeTypeId() != null) {
            EmploymentType employmentType = employmentTypeRepository
                    .findById(request.getEmployeeTypeId())
                    .orElseThrow(() -> new RuntimeException("Employment Type not found"));

            policy.setEmploymentType(employmentType);
        }

        if (request.getUpdatedBy() != null) {
            policy.setUpdatedBy(request.getUpdatedBy());
        }

        policy.setUpdatedOn(Instant.now());

        leavePolicyRepository.save(policy);

        return LeavePolicyResponseDto.builder()
                .message("Leave Policy updated successfully")
                .build();
    }

    @Override
    public LeavePolicyResponseDto deletePolicy(UUID policyId) {

        LeavePolicy policy = leavePolicyRepository.findById(policyId)
                .orElseThrow(() -> new RuntimeException("Leave Policy not found"));

        leavePolicyRepository.delete(policy);

        return LeavePolicyResponseDto.builder()
                .message("Leave Policy deleted successfully")
                .build();
    }

}
