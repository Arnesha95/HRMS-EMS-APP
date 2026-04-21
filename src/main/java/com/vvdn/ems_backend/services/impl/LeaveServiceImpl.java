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
import java.util.*;

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

        Employee emp = leave.getEmployeeLeaves().getEmployee();

        String employeeName = emp.getFirstName() + " " + emp.getLastName();

        return new LeaveApprovalResponseDto(
                employeeName,
                "Leave " + request.getStatus(),
                leave.getCreatedOn().toString()
        );
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

        return LeaveApprovalResponseDto.builder()
                .message("Yearly leave credited successfully")
                .createdOn(Instant.now().toString())
                .build();
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

            Employee emp = leave.getEmployeeLeaves().getEmployee();

            LeaveHistoryResponseDto dto = LeaveHistoryResponseDto.builder()
                    .leaveApplicationId(leave.getLeaveApplicationId())
                    .employeeName(emp.getFirstName() + " " + emp.getLastName())
                    .createdOn(leave.getCreatedOn())
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
                    .employeeName(emp.getFirstName() + " " + emp.getLastName())
                    .createdOn(leave.getCreatedOn())
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
    public LeaveSummaryDto getLeaveSummary() {

        // 🔹 Request counts
        long total = leaveRepo.count();
        long pending = leaveRepo.countByStatus(LeaveStatus.PENDING);
        long approved = leaveRepo.countByStatus(LeaveStatus.APPROVED);
        long rejected = leaveRepo.countByStatus(LeaveStatus.REJECTED);

        // 🔹 Leave totals
        Float allocated = empLeaveRepo.getTotalAllocated();
        Float used = empLeaveRepo.getTotalUsed();
        Float remaining = empLeaveRepo.getTotalRemaining();

        // null safety
        allocated = allocated == null ? 0 : allocated;
        used = used == null ? 0 : used;
        remaining = remaining == null ? 0 : remaining;

        // 🔹 Breakdown by leave type
        List<EmployeeLeave> allLeaves = empLeaveRepo.findAll();

        Map<String, LeaveTypeSummaryDto.LeaveTypeSummaryDtoBuilder> map = new HashMap<>();

        for (EmployeeLeave el : allLeaves) {

            String type = el.getLeavePolicy().getLeaveType().getType();

            map.putIfAbsent(type, LeaveTypeSummaryDto.builder()
                    .leaveType(type)
                    .allocated(0)
                    .used(0)
                    .remaining(0)
                    .pending(0)
                    .approved(0)
                    .rejected(0)
            );

            var dto = map.get(type);

            dto.allocated(dto.build().getAllocated() + el.getTotalLeaves());
            dto.used(dto.build().getUsed() + el.getUsedLeaves());
            dto.remaining(dto.build().getRemaining() + el.getRemainingLeaves());
        }

        // 🔹 Add request counts per type
        List<LeaveApplication> apps = leaveRepo.findAll();

        for (LeaveApplication app : apps) {

            String type = app.getEmployeeLeaves()
                    .getLeavePolicy()
                    .getLeaveType()
                    .getType();

            var dto = map.get(type);

            if (dto == null) continue;

            switch (app.getStatus()) {
                case PENDING -> dto.pending(dto.build().getPending() + 1);
                case APPROVED -> dto.approved(dto.build().getApproved() + 1);
                case REJECTED -> dto.rejected(dto.build().getRejected() + 1);
            }
        }

        List<LeaveTypeSummaryDto> breakdown = map.values()
                .stream()
                .map(builder -> builder.build())
                .toList();

        return LeaveSummaryDto.builder()
                .totalRequests(total)
                .pendingRequests(pending)
                .approvedRequests(approved)
                .rejectedRequests(rejected)
                .totalAllocated(allocated)
                .totalUsed(used)
                .totalRemaining(remaining)
                .leaveTypeBreakdown(breakdown)
                .build();
    }
}





