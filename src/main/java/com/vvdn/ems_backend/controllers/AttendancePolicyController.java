package com.vvdn.ems_backend.controllers;

import com.vvdn.ems_backend.dtos.AttendancePolicyRequestDto;
import com.vvdn.ems_backend.dtos.AttendancePolicyResponseDto;
import com.vvdn.ems_backend.entity.AttendancePolicy;
import com.vvdn.ems_backend.services.AttendancePolicyService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/attendance-policy")
@RequiredArgsConstructor
public class AttendancePolicyController {

    private final AttendancePolicyService attendancePolicyService;

    @PostMapping
    public ResponseEntity<AttendancePolicyResponseDto> createPolicy(
            @RequestBody AttendancePolicyRequestDto request) {

        return new ResponseEntity<>(
                attendancePolicyService.createPolicy(request),
                HttpStatus.CREATED
        );
    }

    @GetMapping("/{attPolicyId}")
    public ResponseEntity<AttendancePolicy> getPolicyById(
            @PathVariable UUID attPolicyId) {

        return ResponseEntity.ok(
                attendancePolicyService.getPolicyById(attPolicyId)
        );
    }

    @GetMapping
    public ResponseEntity<List<AttendancePolicy>> getAllPolicies() {
        return ResponseEntity.ok(
                attendancePolicyService.getAllPolicies()
        );
    }

    @PutMapping("/{attPolicyId}")
    public ResponseEntity<AttendancePolicyResponseDto> updatePolicy(
            @PathVariable UUID attPolicyId,
            @RequestBody AttendancePolicyRequestDto request) {

        return ResponseEntity.ok(
                attendancePolicyService.updatePolicy(attPolicyId, request)
        );
    }

    @DeleteMapping("/{attPolicyId}/deactivate")
    public ResponseEntity<AttendancePolicyResponseDto> deactivatedAttendancePolicy(
            @PathVariable UUID attPolicyId) {

        return ResponseEntity.ok(
                attendancePolicyService.deactivatedAttendancePolicy(attPolicyId)
        );
    }

}
