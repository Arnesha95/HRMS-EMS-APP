package com.vvdn.ems_backend.controllers;

import com.vvdn.ems_backend.dtos.LeavePolicyRequestDto;
import com.vvdn.ems_backend.dtos.LeavePolicyResponseDto;
import com.vvdn.ems_backend.entity.LeavePolicy;
import com.vvdn.ems_backend.services.LeavePolicyService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class LeavePolicyController {

    private final LeavePolicyService leavePolicyService;

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/leave-policy")
    public ResponseEntity<LeavePolicyResponseDto> createPolicy(
            @RequestBody LeavePolicyRequestDto request) {

        return new ResponseEntity<>(
                leavePolicyService.createPolicy(request),
                HttpStatus.CREATED
        );
    }

    @PreAuthorize("hasAnyRole('ADMIN','HR', 'EMPLOYEE')")
    @GetMapping("/leave-policy/{id}")
    public ResponseEntity<LeavePolicy> getPolicyById(
            @PathVariable UUID policyId) {

        return ResponseEntity.ok(
                leavePolicyService.getPolicyById(policyId)
        );
    }


    @PreAuthorize("hasAnyRole('ADMIN', 'HR', 'EMPLOYEE')")
    @GetMapping("/leave-policy")
    public ResponseEntity<List<LeavePolicy>> getAllPolicies() {
        return ResponseEntity.ok(
                leavePolicyService.getAllPolicies()
        );
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/leave-policy/{policyId}")
    public ResponseEntity<LeavePolicyResponseDto> updatePolicy(
            @PathVariable UUID policyId,
            @RequestBody LeavePolicyRequestDto request) {

        return ResponseEntity.ok(
                leavePolicyService.updatePolicy(policyId, request)
        );
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/leave-policy/{policyId}")
    public ResponseEntity<LeavePolicyResponseDto> patchPolicy(
            @PathVariable UUID policyId,
            @RequestBody LeavePolicyRequestDto request) {

        return ResponseEntity.ok(
                leavePolicyService.updatePolicy(policyId, request)
        );
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/leave-policy/{policyId}")
    public ResponseEntity<LeavePolicyResponseDto> deletePolicy(
            @PathVariable UUID policyId) {

        return ResponseEntity.ok(
                leavePolicyService.deletePolicy(policyId)
        );
    }
}
