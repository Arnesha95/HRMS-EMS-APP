package com.vvdn.ems_backend.controllers;

import com.vvdn.ems_backend.dtos.*;
import com.vvdn.ems_backend.exception.BadRequestException;
import com.vvdn.ems_backend.repository.EmpRepository;
import com.vvdn.ems_backend.services.LeaveService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/leaves")
@RequiredArgsConstructor
public class LeaveController {

    private final LeaveService leaveService;
    private final EmpRepository empRepo;



//    private UUID getLoggedInUserId() {
//
//        String email = SecurityContextHolder.getContext()
//                .getAuthentication()
//                .getName();
//
//        return empRepo.findByEmail(email)
//                .orElseThrow(() -> new RuntimeException("User not found"))
//                .getEmpId();
//    }

    private UUID getLoggedInUserId() {

        var context = SecurityContextHolder.getContext();
        var authentication = context.getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            throw new BadRequestException("User not authenticated");
        }

        String email = authentication.getName();

        return empRepo.findByEmail(email)
                .orElseThrow(() -> new BadRequestException("User not found"))
                .getEmpId();
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'HR', 'EMPLOYEE')")
    @PostMapping("/apply")
    public ResponseEntity<ApplyLeaveResponseDto> applyLeave(
            @RequestBody ApplyLeaveRequestDto request
    ) {
        UUID loggedInUserId = getLoggedInUserId();

        return ResponseEntity.ok(
                leaveService.applyLeave(request, loggedInUserId)
        );
    }


//    @PostMapping("/approve-reject")
//    public ResponseEntity<LeaveApprovalResponseDto> approveReject(
//            @RequestBody LeaveApprovalRequestDto request
//    ) {
//        return ResponseEntity.ok(
//                leaveService.approveOrReject(request, request.getHrId())
//        );
//    }

    @PreAuthorize("hasRole('HR')")
    @PostMapping("/approve-reject")
    public ResponseEntity<LeaveApprovalResponseDto> approveReject(
            @RequestBody LeaveApprovalRequestDto request
    ) {
        UUID loggedInUserId = getLoggedInUserId(); // from security context

        return ResponseEntity.ok(
                leaveService.approveOrReject(request, loggedInUserId)
        );
    }

    @PreAuthorize("hasAnyRole('EMPLOYEE', 'ADMIN', 'HR')")
    @PostMapping("/cancel")
    public ResponseEntity<CancelLeaveResponseDto> cancelLeave(
            @RequestBody CancelLeaveRequestDto request
    ) {
        UUID userId = getLoggedInUserId();

        return ResponseEntity.ok(
                leaveService.cancelLeave(request, userId)
        );
    }



    @PreAuthorize("hasRole('HR')")
    @PostMapping("/credit-yearly")
    public ResponseEntity<LeaveApprovalResponseDto> creditYearly(@RequestParam int year) {
        return ResponseEntity.ok(leaveService.creditYearlyLeaves(year));
    }


    @PreAuthorize("hasAnyRole('ADMIN', 'HR', 'EMPLOYEE')")
    @GetMapping("/balance/{empId}")
    public List<EmployeeLeaveResponseDto> getLeaveBalance(@PathVariable UUID empId) {
        return leaveService.getEmployeeLeaveBalance(empId);
    }


    @PreAuthorize("hasAnyRole('ADMIN', 'HR', 'EMPLOYEE')")
    @GetMapping("/history")
    public ResponseEntity<List<LeaveHistoryResponseDto>> getLeaveHistory(
            @RequestParam UUID empId) {

        List<LeaveHistoryResponseDto> response = leaveService.getLeaveHistory(empId);

        if (response.isEmpty()) {
            return ResponseEntity.noContent().build(); // 204
        }

        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasAnyRole('HR', 'ADMIN', 'EMPLOYEE')")
    @GetMapping("/requests")
    public ResponseEntity<List<LeaveHistoryResponseDto>> getAllLeaveRequests(
            @RequestParam(required = false) String status
    ) {
        return ResponseEntity.ok(
                leaveService.getAllLeaveRequests(status)
        );
    }


    @PreAuthorize("hasAnyRole('ADMIN', 'HR', 'EMPLOYEE')")
    @GetMapping("/summary")
    public ResponseEntity<LeaveSummaryDto> getSummary() {
        return ResponseEntity.ok(leaveService.getLeaveSummary());
    }


    @PreAuthorize("hasAnyRole('ADMIN', 'HR', 'EMPLOYEE')")
    @GetMapping("/{leaveApplicationId}")
    public ResponseEntity<LeaveDetailsResponseDto> getLeaveById(
            @PathVariable UUID leaveApplicationId) {

        return ResponseEntity.ok(
                leaveService.getLeaveById(leaveApplicationId)
        );
    }
}
