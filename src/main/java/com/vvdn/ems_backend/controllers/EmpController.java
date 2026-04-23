package com.vvdn.ems_backend.controllers;

import com.vvdn.ems_backend.dtos.EmpRequestDto;
import com.vvdn.ems_backend.dtos.EmpResponseDto;
import com.vvdn.ems_backend.dtos.EmployeeEventDto;
import com.vvdn.ems_backend.dtos.EmployeeSummaryDto;
import com.vvdn.ems_backend.entity.Employee;
import com.vvdn.ems_backend.services.EmpService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/employees")
@RequiredArgsConstructor
public class EmpController {

    private final EmpService service;

    @PreAuthorize("hasAnyRole('ADMIN', 'HR')")
    @PostMapping("/onboarding")
    public EmpResponseDto addEmployee(@RequestBody EmpRequestDto request) {
        return service.addEmployee(request);
    }


    @PreAuthorize("hasRole('HR')")
    @PutMapping("/{empId}")
    public EmpResponseDto updateEmployee(@PathVariable UUID empId,
                                              @RequestBody EmpRequestDto request) {

        return service.updateEmployee(empId, request);
    }


    @PreAuthorize("hasRole('HR')")
    @DeleteMapping("/{empId}")
    public EmpResponseDto deactivateEmployee(@PathVariable UUID empId) {
        return service.deactivateEmployee(empId);
    }


    @PreAuthorize("hasAnyRole('HR', 'EMPLOYEE', 'ADMIN')")
    @GetMapping("/{empId}")
    public Employee getEmployeeById(@PathVariable UUID empId) {
        return service.getEmployeeById(empId);
    }


    @PreAuthorize("hasAnyRole('ADMIN', 'HR', 'EMPLOYEE')")
    @GetMapping
    public List<Employee> getAllEmployees() {
        return service.getAllEmployees();
    }


    @PreAuthorize("hasAnyRole('ADMIN', 'HR', 'EMPLOYEE')")
    @GetMapping("/summary")
    public EmployeeSummaryDto getSummary() {
        return service.getEmployeeSummary();
    }


    @PreAuthorize("hasAnyRole('HR', 'EMPLOYEE', 'ADMIN')")
    @GetMapping("/birthdays/today")
    public ResponseEntity<List<EmployeeEventDto>> getTodaysBirthdays() {
        return ResponseEntity.ok(service.getTodaysBirthdays());
    }

    @PreAuthorize("hasAnyRole('HR', 'EMPLOYEE', 'ADMIN')")
    @GetMapping("/anniversaries/today")
    public ResponseEntity<List<EmployeeEventDto>> getTodaysAnniversaries() {
        return ResponseEntity.ok(service.getTodaysAnniversaries());
    }

}
