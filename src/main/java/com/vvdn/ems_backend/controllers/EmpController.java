package com.vvdn.ems_backend.controllers;

import com.vvdn.ems_backend.dtos.EmpRequestDto;
import com.vvdn.ems_backend.dtos.EmpResponseDto;
import com.vvdn.ems_backend.entity.Employee;
import com.vvdn.ems_backend.services.EmpService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class EmpController {

    private final EmpService service;

    @PostMapping("/employees/onboarding")
    public EmpResponseDto addEmployee(@RequestBody EmpRequestDto request) {
        return service.addEmployee(request);
    }


    @PutMapping("/employees/{id}")
    public EmpResponseDto updateEmployee(@PathVariable UUID id,
                                              @RequestBody EmpRequestDto request) {

        return service.updateEmployee(id, request);
    }

    @DeleteMapping("/employees/{id}")
    public EmpResponseDto deactivateEmployee(@PathVariable UUID id) {
        return service.deactivateEmployee(id);
    }

    @GetMapping("/employees/{id}")
    public Employee getEmployeeById(@PathVariable UUID id) {
        return service.getEmployeeById(id);
    }

    @GetMapping("/employees")
    public List<Employee> getAllEmployees() {
        return service.getAllEmployees();
    }
}
