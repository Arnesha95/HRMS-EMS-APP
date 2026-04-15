package com.vvdn.ems_backend.controllers;


import com.vvdn.ems_backend.dtos.EmployeeLeaveRequestDto;
import com.vvdn.ems_backend.dtos.EmployeeLeaveResponseDto;
import com.vvdn.ems_backend.services.EmployeeLeaveService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class EmployeeLeaveController {

    private final EmployeeLeaveService employeeLeaveService;


    @PostMapping("/employee-leaves/allocate")
    public List<EmployeeLeaveResponseDto> allocateLeaves(
            @RequestBody EmployeeLeaveRequestDto request) {

        return employeeLeaveService.allocateLeaves(request);
    }


    @GetMapping("/employee-leaves/{empId}")
    public List<EmployeeLeaveResponseDto> getEmployeeLeaves(
            @PathVariable UUID empId) {

        return employeeLeaveService.getEmployeeLeaves(empId);
    }
}


