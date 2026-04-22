package com.vvdn.ems_backend.services;

import com.vvdn.ems_backend.dtos.*;

import java.util.List;
import java.util.UUID;

public interface LeaveService {

    ApplyLeaveResponseDto applyLeave(ApplyLeaveRequestDto request, UUID userId);

    LeaveApprovalResponseDto approveOrReject(LeaveApprovalRequestDto request, UUID hrId);

    CancelLeaveResponseDto cancelLeave(CancelLeaveRequestDto request, UUID userId);

    LeaveApprovalResponseDto creditYearlyLeaves(int year);

    List<EmployeeLeaveResponseDto> getEmployeeLeaveBalance(UUID empId);

    List<LeaveHistoryResponseDto> getLeaveHistory(UUID empId);

    List<LeaveHistoryResponseDto> getAllLeaveRequests(String status);

    LeaveSummaryDto getLeaveSummary();

    LeaveDetailsResponseDto getLeaveById(UUID leaveApplicationId);

}
