package com.vvdn.ems_backend.services;

import com.vvdn.ems_backend.dtos.EmpRequestDto;
import com.vvdn.ems_backend.dtos.EmpResponseDto;
import com.vvdn.ems_backend.entity.Employee;

import java.util.List;
import java.util.UUID;

public interface EmpService {

    EmpResponseDto addEmployee(EmpRequestDto request);

    EmpResponseDto updateEmployee(UUID id, EmpRequestDto request);

    EmpResponseDto deactivateEmployee(UUID id);

    Employee getEmployeeById(UUID id);

    List<Employee> getAllEmployees();
}
