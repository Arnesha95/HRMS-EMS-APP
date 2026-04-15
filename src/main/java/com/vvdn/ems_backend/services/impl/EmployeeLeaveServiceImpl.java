package com.vvdn.ems_backend.services.impl;

import com.vvdn.ems_backend.dtos.EmployeeLeaveRequestDto;
import com.vvdn.ems_backend.dtos.EmployeeLeaveResponseDto;
import com.vvdn.ems_backend.entity.*;
import com.vvdn.ems_backend.exception.BadRequestException;
import com.vvdn.ems_backend.repository.*;
import com.vvdn.ems_backend.services.EmployeeLeaveService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EmployeeLeaveServiceImpl implements EmployeeLeaveService {

    private final EmployeeLeaveRepository employeeLeaveRepository;
    private final EmpRepository empRepository;
    private final LeavePolicyRepository leavePolicyRepository;

    @Transactional
    @Override
    public List<EmployeeLeaveResponseDto> allocateLeaves(EmployeeLeaveRequestDto request) {

        Employee employee = empRepository.findById(request.getEmpId())
                .orElseThrow(() -> new BadRequestException("Employee not found"));

        UUID empTypeId = employee.getEmploymentType().getId();

        List<LeavePolicy> policies =
                leavePolicyRepository.findByEmploymentType_IdAndYear(empTypeId, request.getYear());

        if (policies.isEmpty()) {
            throw new BadRequestException("No leave policies found for this employee type and year");
        }

        List<EmployeeLeave> savedLeaves = new ArrayList<>();

        for (LeavePolicy policy : policies) {

            boolean exists = employeeLeaveRepository
                    .existsByEmployeeAndLeavePolicy(employee, policy);

            if (exists) {
                continue; // skip duplicates
            }

            float yearlyLeaves = policy.getNoOfDays();

            float proratedLeaves = calculateProratedLeaves(
                    yearlyLeaves,
                    request.getJoiningDate(),
                    request.getYear()
            );

            EmployeeLeave employeeLeave = EmployeeLeave.builder()
                    .employee(employee)
                    .leavePolicy(policy)
                    .totalLeaves(proratedLeaves)
                    .usedLeaves(0f)
                    .remainingLeaves(proratedLeaves)
                    .createdBy(request.getCreatedBy())
                    .build();

            savedLeaves.add(employeeLeave);
        }

        employeeLeaveRepository.saveAll(savedLeaves);

        return savedLeaves.stream()
                .map(l -> mapToDto(l, request.getYear()))
                .toList();
    }

    @Override
    public List<EmployeeLeaveResponseDto> getEmployeeLeaves(UUID empId) {

        List<EmployeeLeave> leaves = employeeLeaveRepository.findByEmployee_EmpId(empId);

        return leaves.stream()
                .map(l -> mapToDto(l, null))
                .collect(Collectors.toList());
    }


    private float calculateProratedLeaves(float yearlyLeaves, LocalDate joiningDate, short year) {

        if (joiningDate.getYear() < year) {
            return yearlyLeaves; // full leaves
        }

        int joiningMonth = joiningDate.getMonthValue();

        int remainingMonths = 12 - joiningMonth + 1;

        float prorated = (yearlyLeaves / 12) * remainingMonths;

        return Math.round(prorated * 100f) / 100f;
    }


    private EmployeeLeaveResponseDto mapToDto(EmployeeLeave leave, Short year) {

        return EmployeeLeaveResponseDto.builder()
                .empLeaveId(leave.getEmpLeaveId())
                .leaveType(leave.getLeavePolicy().getLeaveType().getType())
                .totalLeaves(leave.getTotalLeaves())
                .usedLeaves(leave.getUsedLeaves())
                .remainingLeaves(leave.getRemainingLeaves())
                .year(year)
                .build();
    }
}
