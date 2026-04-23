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
import java.time.temporal.ChronoUnit;
import java.util.*;

@Service
@RequiredArgsConstructor
public class LeaveServiceImpl implements LeaveService {

    private final LeaveRepository leaveRepo;
    private final EmployeeLeaveRepository empLeaveRepo;
    private final LeavePolicyRepository leavePolicyRepository;
    private final EmpRepository employeeRepository;


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


        LeavePolicy policy = empLeaves.getLeavePolicy();
        LeaveType leaveType = policy.getLeaveType();

        long days = ChronoUnit.DAYS.between(request.getStartDate(), request.getEndDate()) + 1;

        if (leaveType.getMaxConsecutiveDays() != null &&
                days > leaveType.getMaxConsecutiveDays()) {

            throw new BadRequestException(
                    "Cannot apply more than " + leaveType.getMaxConsecutiveDays() + " consecutive days for " + leaveType.getType()
            );
        }

        if (!empLeaves.getEmployee().getEmpId().equals(userId)) {
            throw new BadRequestException("You are not allowed to apply leave for this employee");
        }

        
        if (!leaveType.getPostApplicationAllowed() &&
                request.getStartDate().isBefore(LocalDate.now())) {

            throw new BadRequestException("Past leave application not allowed for " + leaveType.getType());
        }

        if (leaveRepo.existsByEmployeeLeaves_Employee_EmpIdAndStatusInAndStartDateLessThanEqualAndEndDateGreaterThanEqual(
                userId,
                List.of(LeaveStatus.PENDING, LeaveStatus.APPROVED),
                request.getEndDate(),
                request.getStartDate()
        )) {
            throw new BadRequestException("Leave already exists for selected dates");
        }


        empLeaves.setUsedLeaves(empLeaves.getUsedLeaves() + request.getNoOfDays());
        empLeaves.setRemainingLeaves(empLeaves.getRemainingLeaves() - request.getNoOfDays());
        empLeaves.setUpdatedBy(userId);
        empLeaves.setUpdatedOn(Instant.now());

        empLeaveRepo.save(empLeaves);


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


        List<LeaveStatus> activeStatuses = List.of(
                LeaveStatus.PENDING,
                LeaveStatus.APPROVED
        );

//        boolean exists = leaveRepo
//                .existsByEmployeeLeaves_Employee_EmpIdAndStatusInAndStartDateLessThanEqualAndEndDateGreaterThanEqual(
//                        empLeaves.getEmployee().getEmpId(),
//                        List.of(LeaveStatus.PENDING),
//                        request.getEndDate(),
//                        request.getStartDate()
//                );
//
//        if (exists) {
//            throw new BadRequestException(
//                    "Leave already applied for selected dates (Pending request exists)");
//        }

        return new ApplyLeaveResponseDto("Leave applied successfully");
    }

    @Transactional
    @Override
    public LeaveApprovalResponseDto approveOrReject(LeaveApprovalRequestDto request, UUID hrId) {

        LeaveApplication leave = leaveRepo.findById(request.getLeaveApplicationId())
                .orElseThrow(() -> new RuntimeException("Leave not found"));


        Employee emp = leave.getEmployeeLeaves().getEmployee();


        if (leave.getStatus() != LeaveStatus.PENDING) {
            throw new BadRequestException("Leave already processed");
        }

        if (emp.getEmpId().equals(hrId)) {
            throw new BadRequestException("You cannot approve/reject your own leave");
        }

        EmployeeLeave empLeaves = leave.getEmployeeLeaves();

        LeaveStatus newStatus;

        try {
            newStatus = LeaveStatus.valueOf(request.getStatus().toUpperCase());
        } catch (Exception e) {
            throw new BadRequestException("Invalid status value");
        }

        if (newStatus == LeaveStatus.REJECTED) {

            float updatedUsed = empLeaves.getUsedLeaves() - leave.getNoOfDays();
            float updatedRemaining = empLeaves.getRemainingLeaves() + leave.getNoOfDays();

            // Safety guards
            if (updatedUsed < 0) {
                updatedUsed = 0;
            }

            if (updatedRemaining > empLeaves.getTotalLeaves()) {
                updatedRemaining = empLeaves.getTotalLeaves();
            }

            empLeaves.setUsedLeaves(updatedUsed);
            empLeaves.setRemainingLeaves(updatedRemaining);
            empLeaves.setUpdatedBy(hrId);
            empLeaves.setUpdatedOn(Instant.now());

            empLeaveRepo.save(empLeaves);
        }


        leave.setStatus(newStatus);
        leave.setAppRejBy(hrId);
        leave.setAppRejOn(LocalDate.now());
        leave.setRemarks(request.getRemarks());

        leaveRepo.save(leave);


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

//        if (applications.isEmpty()) {
//            throw new BadRequestException("No leave history found");
//        }

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


        long total = leaveRepo.count();
        long pending = leaveRepo.countByStatus(LeaveStatus.PENDING);
        long approved = leaveRepo.countByStatus(LeaveStatus.APPROVED);
        long rejected = leaveRepo.countByStatus(LeaveStatus.REJECTED);


        Float allocated = empLeaveRepo.getTotalAllocated();
        Float used = empLeaveRepo.getTotalUsed();
        Float remaining = empLeaveRepo.getTotalRemaining();


        allocated = allocated == null ? 0 : allocated;
        used = used == null ? 0 : used;
        remaining = remaining == null ? 0 : remaining;


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

    @Override
    public LeaveDetailsResponseDto getLeaveById(UUID leaveApplicationId) {

        LeaveApplication leave = leaveRepo.findById(leaveApplicationId)
                .orElseThrow(() -> new BadRequestException("Leave not found"));

        Employee emp = leave.getEmployeeLeaves().getEmployee();

        String employeeName = emp.getFirstName() + " " + emp.getLastName();

        String leaveType = leave.getEmployeeLeaves()
                .getLeavePolicy()
                .getLeaveType()
                .getType();


        LocalDate approvedOn = null;
        LocalDate rejectedOn = null;
        UUID approvedBy = null;
        UUID rejectedBy = null;



        if (leave.getStatus() == LeaveStatus.APPROVED) {
            approvedOn = leave.getAppRejOn();
            approvedBy = leave.getAppRejBy();
        } else if (leave.getStatus() == LeaveStatus.REJECTED) {
            rejectedOn = leave.getAppRejOn();
            rejectedBy = leave.getAppRejBy();
        }

        String approverName = null;
        String denierName = null;

        UUID actionBy = leave.getAppRejBy();

        if (actionBy != null) {
            Employee actionEmp = employeeRepository.findById(actionBy).orElse(null);

            if (actionEmp != null) {
                String fullName = actionEmp.getFirstName() + " " + actionEmp.getLastName();

                if (leave.getStatus() == LeaveStatus.APPROVED) {
                    approverName = fullName;
                } else if (leave.getStatus() == LeaveStatus.REJECTED) {
                    denierName = fullName;
                }
            }
        }

        return LeaveDetailsResponseDto.builder()
                .leaveApplicationId(leave.getLeaveApplicationId())
                .employeeName(employeeName)
                .leaveType(leaveType)
                .noOfDays(leave.getNoOfDays())
                .startDate(leave.getStartDate())
                .endDate(leave.getEndDate())
                .status(leave.getStatus().name())
                .createdOn(leave.getCreatedOn())
                .approvedOn(approvedOn)
                .rejectedOn(rejectedOn)
                .approvedBy(approvedBy)
                .approver(approverName)
                .denier(denierName)
                .rejectedBy(rejectedBy)
                .remarks(leave.getRemarks())
                .build();
    }

    @Override
    @Transactional
    public CancelLeaveResponseDto cancelLeave(CancelLeaveRequestDto request, UUID userId) {

        LeaveApplication leave = leaveRepo.findById(request.getLeaveApplicationId())
                .orElseThrow(() -> new BadRequestException("Leave not found"));

        EmployeeLeave empLeaves = leave.getEmployeeLeaves();

        // ownership check
        if (!empLeaves.getEmployee().getEmpId().equals(userId)) {
            throw new BadRequestException("You can only cancel your own leave");
        }

        // already processed?
        if (leave.getStatus() == LeaveStatus.CANCELLED) {
            throw new BadRequestException("Leave already cancelled");
        }

        if (leave.getStatus() == LeaveStatus.REJECTED) {
            throw new BadRequestException("Rejected leave cannot be cancelled");
        }

// restore balance for BOTH pending and approved
        if (leave.getStatus() == LeaveStatus.APPROVED
                || leave.getStatus() == LeaveStatus.PENDING) {

            empLeaves.setUsedLeaves(empLeaves.getUsedLeaves() - leave.getNoOfDays());
            empLeaves.setRemainingLeaves(empLeaves.getRemainingLeaves() + leave.getNoOfDays());

            // safety bounds
            if (empLeaves.getUsedLeaves() < 0) {
                empLeaves.setUsedLeaves(0f);
            }

            if (empLeaves.getRemainingLeaves() > empLeaves.getTotalLeaves()) {
                empLeaves.setRemainingLeaves(empLeaves.getTotalLeaves());
            }

            empLeaves.setUpdatedBy(userId);
            empLeaves.setUpdatedOn(Instant.now());

            empLeaveRepo.save(empLeaves);

        }

        // mark leave cancelled
        leave.setStatus(LeaveStatus.CANCELLED);
        leave.setRemarks(request.getRemarks());
        leave.setAppRejBy(userId);
        leave.setAppRejOn(LocalDate.now());

        leaveRepo.save(leave);

        return CancelLeaveResponseDto.builder()
                .message("Leave cancelled successfully")
                .cancelledOn(Instant.now())
                .build();
    }
}





