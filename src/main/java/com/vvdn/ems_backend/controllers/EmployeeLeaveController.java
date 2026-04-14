package com.vvdn.ems_backend.controllers;

import com.vvdn.ems_backend.dtos.EmployeeLeaveResponseDto;
import com.vvdn.ems_backend.services.EmployeeLeaveService;
import com.vvdn.ems_backend.services.LeaveService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class EmployeeLeaveController {

    private final EmployeeLeaveService employeeLeaveService;
    private final LeaveService leaveService;

    @PostMapping("/employee-leaves/allocate/{empId}")
    public ResponseEntity<?> allocateLeavesToEmployee(@PathVariable UUID empId) {

        List<EmployeeLeaveResponseDto> response =
                employeeLeaveService.allocateLeavesToEmployee(empId);

        return ResponseEntity.ok(Map.of(
                "message", "Leave allocated successfully",
                "data", response
        ));
    }

}

