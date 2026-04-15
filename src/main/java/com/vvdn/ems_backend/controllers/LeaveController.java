package com.vvdn.ems_backend.controllers;

import com.vvdn.ems_backend.dtos.*;
import com.vvdn.ems_backend.services.LeaveService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class LeaveController {

    private final LeaveService leaveService;

    @PostMapping("/leaves/apply")
    public ResponseEntity<?> applyLeave(
            @RequestBody ApplyLeaveRequestDto request
    ) {
        return ResponseEntity.ok(
                leaveService.applyLeave(request, UUID.randomUUID())
        );
    }

    @PostMapping("/leaves/approve-reject")
    public ResponseEntity<LeaveApprovalResponseDto> approveReject(
            @RequestBody LeaveApprovalRequestDto request
    ) {
        return ResponseEntity.ok(
                leaveService.approveOrReject(request, UUID.randomUUID())
        );
    }

    @PostMapping("/leaves/credit-yearly")
    public ResponseEntity<LeaveApprovalResponseDto> creditYearly(@RequestParam int year) {
        return ResponseEntity.ok(leaveService.creditYearlyLeaves(year));
    }

    @GetMapping("/leaves/balance/{empId}")
    public List<EmployeeLeaveResponseDto> getLeaveBalance(@PathVariable UUID empId) {
        return leaveService.getEmployeeLeaveBalance(empId);
    }

    @GetMapping("/leaves/history")
    public ResponseEntity<List<LeaveHistoryResponseDto>> getLeaveHistory(
            @RequestParam UUID empId) {

        return ResponseEntity.ok(leaveService.getLeaveHistory(empId));
    }

}
