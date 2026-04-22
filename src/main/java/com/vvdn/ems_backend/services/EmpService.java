package com.vvdn.ems_backend.services;

import com.vvdn.ems_backend.dtos.EmpRequestDto;
import com.vvdn.ems_backend.dtos.EmpResponseDto;
import com.vvdn.ems_backend.dtos.EmployeeEventDto;
import com.vvdn.ems_backend.dtos.EmployeeSummaryDto;
import com.vvdn.ems_backend.entity.Employee;

import java.util.List;
import java.util.UUID;

public interface EmpService {

    EmpResponseDto addEmployee(EmpRequestDto request);

    EmpResponseDto updateEmployee(UUID empId, EmpRequestDto request);

    EmpResponseDto deactivateEmployee(UUID EmpId);

    Employee getEmployeeById(UUID EmpId);

    List<Employee> getAllEmployees();

    EmployeeSummaryDto getEmployeeSummary();

    List<EmployeeEventDto> getTodaysBirthdays();
    List<EmployeeEventDto> getTodaysAnniversaries();
}
