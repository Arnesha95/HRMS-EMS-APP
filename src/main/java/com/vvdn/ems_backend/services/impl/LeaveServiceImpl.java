package com.vvdn.ems_backend.services.impl;

import com.vvdn.ems_backend.dtos.*;
import com.vvdn.ems_backend.entity.*;
import com.vvdn.ems_backend.exception.BadRequestException;
import com.vvdn.ems_backend.repository.EmpRepository;
import com.vvdn.ems_backend.repository.EmployeeLeaveRepository;
import com.vvdn.ems_backend.repository.LeaveRepository;
import com.vvdn.ems_backend.repository.LeavePolicyRepository;
import com.vvdn.ems_backend.services.LeaveService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class LeaveServiceImpl implements LeaveService {

    private final LeaveRepository leaveRepo;
    private final EmployeeLeaveRepository empLeaveRepo;
    private final LeavePolicyRepository leavePolicyRepository;
    private final EmpRepository employeeRepository;
    private final LeaveRepository leaveRepository;


    @Override
    public ApplyLeaveResponseDto applyLeave(ApplyLeaveRequestDto request, UUID userId) {

        EmployeeLeave empLeaves = empLeaveRepo.findById(request.getEmpLeaveId())
                .orElseThrow(() -> new RuntimeException("Employee leave record not found"));


        if (empLeaves.getRemainingLeaves() < request.getNoOfDays()) {
            throw new BadRequestException("Insufficient leave balance");
        }

        if (request.getStartDate().isAfter(request.getEndDate())) {
            throw new BadRequestException("Invalid date range");
        }


        LeaveApplication leave = LeaveApplication.builder()
                .employeeLeaves(empLeaves)
                .status(LeaveStatus.PENDING)
                .leaveDay(request.getLeaveDay())
                .description(request.getDescription())
                .noOfDays(request.getNoOfDays())
                .startDate(request.getStartDate())
                .endDate(request.getEndDate())
                .createdBy(userId)
                .createdOn(Instant.now())
                .build();

        leaveRepo.save(leave);

        return new ApplyLeaveResponseDto("Leave applied successfully");
    }

    @Transactional
    @Override
    public LeaveApprovalResponseDto approveOrReject(LeaveApprovalRequestDto request, UUID hrId) {

        LeaveApplication leave = leaveRepo.findById(request.getLeaveApplicationId())
                .orElseThrow(() -> new RuntimeException("Leave not found"));


        if (leave.getStatus() != LeaveStatus.PENDING) {
            throw new BadRequestException("Leave already processed");
        }

        EmployeeLeave empLeaves = leave.getEmployeeLeaves();

        LeaveStatus newStatus;

        try {
            newStatus = LeaveStatus.valueOf(request.getStatus().toUpperCase());
        } catch (Exception e) {
            throw new BadRequestException("Invalid status value");
        }

        if (newStatus == LeaveStatus.APPROVED) {

            if (empLeaves.getRemainingLeaves() < leave.getNoOfDays()) {
                throw new BadRequestException("Not enough leave balance");
            }

            empLeaves.setUsedLeaves(empLeaves.getUsedLeaves() + leave.getNoOfDays());
            empLeaves.setRemainingLeaves(empLeaves.getRemainingLeaves() - leave.getNoOfDays());
            empLeaves.setUpdatedBy(hrId);
            empLeaves.setUpdatedOn(Instant.now());

            empLeaveRepo.save(empLeaves);
        }

        leave.setStatus(newStatus);
        leave.setAppRejBy(hrId);
        leave.setAppRejOn(LocalDate.now());
        leave.setRemarks(request.getRemarks());

        leaveRepo.save(leave);

        return new LeaveApprovalResponseDto("Leave " + request.getStatus());
    }


    @Override
    @Transactional
    public LeaveApprovalResponseDto creditYearlyLeaves(int year) {

        List<Employee> employees = employeeRepository.findAll();
        List<LeavePolicy> policies = leavePolicyRepository.findByYear(year);

        List<EmployeeLeave> toSave = new ArrayList<>();

        for (Employee emp : employees) {

            for (LeavePolicy policy : policies) {

                LeaveType type = policy.getLeaveType();


                Optional<EmployeeLeave> existing = empLeaveRepo
                        .findByEmployee_EmpIdAndLeavePolicy_PolicyId(
                                emp.getEmpId(), policy.getPolicyId());

                if (existing.isPresent()) {
                    continue;
                }

                float carryForward = 0f;

                if (type.getCarryForwardAllowed()) {

                    Optional<EmployeeLeave> prevRecord =
                            empLeaveRepo
                                    .findTopByEmployee_EmpIdAndLeavePolicy_LeaveType_TypeIdOrderByCreatedOnDesc(
                                            emp.getEmpId(),
                                            type.getTypeId()
                                    );

                    if (prevRecord.isPresent()) {
                        carryForward = prevRecord.get().getRemainingLeaves();
                    }
                }

                float newTotal = policy.getNoOfDays() + carryForward;

                EmployeeLeave newLeave = EmployeeLeave.builder()
                        .employee(emp)
                        .leavePolicy(policy)
                        .totalLeaves(newTotal)
                        .usedLeaves(0f)
                        .remainingLeaves(newTotal)
                        .createdOn(Instant.now())
                        .updatedOn(Instant.now())
                        .build();

                toSave.add(newLeave);
            }
        }

        empLeaveRepo.saveAll(toSave);

        return new LeaveApprovalResponseDto("Yearly leave credited successfully");
    }

    @Override
    public List<EmployeeLeaveResponseDto> getEmployeeLeaveBalance(UUID empId) {

        List<EmployeeLeave> empLeaves = empLeaveRepo.findByEmployee_EmpId(empId);

        if (empLeaves.isEmpty()) {
            throw new BadRequestException("No leave records found for employee");
        }

        List<EmployeeLeaveResponseDto> response = new ArrayList<>();

        for (EmployeeLeave leave : empLeaves) {

            LeavePolicy policy = leave.getLeavePolicy();
            LeaveType type = policy.getLeaveType();

            EmployeeLeaveResponseDto dto = EmployeeLeaveResponseDto.builder()
                    .empLeaveId(leave.getEmpLeaveId())
                    .leaveType(type.getType())
                    .totalLeaves(leave.getTotalLeaves())
                    .usedLeaves(leave.getUsedLeaves())
                    .remainingLeaves(leave.getRemainingLeaves())
                    .year(policy.getYear())
                    .build();

            response.add(dto);
        }

        return response;

    }

    @Override
    public List<LeaveHistoryResponseDto> getLeaveHistory(UUID empId) {

        List<LeaveApplication> applications =
                leaveRepo.findByEmployeeLeaves_Employee_EmpIdOrderByCreatedOnDesc(empId);

        if (applications.isEmpty()) {
            throw new BadRequestException("No leave history found");
        }

        List<LeaveHistoryResponseDto> response = new ArrayList<>();

        for (LeaveApplication leave : applications) {

            LeaveType type = leave.getEmployeeLeaves()
                    .getLeavePolicy()
                    .getLeaveType();

            LeaveHistoryResponseDto dto = LeaveHistoryResponseDto.builder()
                    .leaveApplicationId(leave.getLeaveApplicationId())
                    .leaveType(type.getType())
                    .noOfDays(leave.getNoOfDays())
                    .startDate(leave.getStartDate())
                    .endDate(leave.getEndDate())
                    .status(leave.getStatus().name())
                    .remarks(leave.getRemarks())
                    .build();

            response.add(dto);
        }

        return response;
    }



    @Override
    public List<LeaveHistoryResponseDto> getAllLeaveRequests(String status) {

        List<LeaveApplication> applications;

        if (status == null || status.equalsIgnoreCase("ALL")) {
            applications = leaveRepo.findAllByOrderByCreatedOnDesc();
        } else {
            LeaveStatus leaveStatus;

            try {
                leaveStatus = LeaveStatus.valueOf(status.toUpperCase());
            } catch (Exception e) {
                throw new BadRequestException("Invalid status");
            }

            applications = leaveRepo.findByStatusOrderByCreatedOnDesc(leaveStatus);
        }

        if (applications.isEmpty()) {
            throw new BadRequestException("No leave requests found");
        }

        List<LeaveHistoryResponseDto> response = new ArrayList<>();

        for (LeaveApplication leave : applications) {

            LeaveType type = leave.getEmployeeLeaves()
                    .getLeavePolicy()
                    .getLeaveType();

            Employee emp = leave.getEmployeeLeaves().getEmployee();

            LeaveHistoryResponseDto dto = LeaveHistoryResponseDto.builder()
                    .leaveApplicationId(leave.getLeaveApplicationId())
                    .leaveType(type.getType())
                    .noOfDays(leave.getNoOfDays())
                    .startDate(leave.getStartDate())
                    .endDate(leave.getEndDate())
                    .status(leave.getStatus().name()) // ✅ convert enum → String for response
                    .remarks(leave.getRemarks())
                    .build();

            response.add(dto);
        }

        return response;
    }
}





