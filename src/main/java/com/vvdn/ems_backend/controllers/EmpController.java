package com.vvdn.ems_backend.controllers;

import com.vvdn.ems_backend.dtos.EmpRequestDto;
import com.vvdn.ems_backend.dtos.EmpResponseDto;
import com.vvdn.ems_backend.dtos.EmployeeSummaryDto;
import com.vvdn.ems_backend.entity.Employee;
import com.vvdn.ems_backend.services.EmpService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/employees")
@RequiredArgsConstructor
public class EmpController {

    private final EmpService service;

    @PostMapping("/onboarding")
    public EmpResponseDto addEmployee(@RequestBody EmpRequestDto request) {
        return service.addEmployee(request);
    }


    @PutMapping("/{empId}")
    public EmpResponseDto updateEmployee(@PathVariable UUID empId,
                                              @RequestBody EmpRequestDto request) {

        return service.updateEmployee(empId, request);
    }


    @DeleteMapping("/{empId}")
    public EmpResponseDto deactivateEmployee(@PathVariable UUID empId) {
        return service.deactivateEmployee(empId);
    }


    @GetMapping("/{empId}")
    public Employee getEmployeeById(@PathVariable UUID empId) {
        return service.getEmployeeById(empId);
    }


    @GetMapping
    public List<Employee> getAllEmployees() {
        return service.getAllEmployees();
    }

    @GetMapping("/summary")
    public EmployeeSummaryDto getSummary() {
        return service.getEmployeeSummary();
    }

}
