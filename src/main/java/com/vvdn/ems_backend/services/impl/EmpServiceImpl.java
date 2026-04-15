package com.vvdn.ems_backend.services.impl;

import com.vvdn.ems_backend.dtos.EmpRequestDto;
import com.vvdn.ems_backend.dtos.EmpResponseDto;
import com.vvdn.ems_backend.entity.*;
import com.vvdn.ems_backend.repository.*;
import com.vvdn.ems_backend.services.EmpService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class EmpServiceImpl implements EmpService {

    private final EmpRepository repository;
    private final UserRepository userRepository;
    private final DepartmentRepository departmentRepository;
    private final DesignationRepository designationRepository;
    private final EmploymentTypeRepository employmentTypeRepository;

    private String generateDefaultPassword() {
        return "Emp@" + System.currentTimeMillis();
    }

    @Bean
    public PasswordEncoder onboadingPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    private final PasswordEncoder onboardingPasswordEncoder;

    @Override
    public EmpResponseDto addEmployee(EmpRequestDto request) {
        Department department = departmentRepository.findById(request.getDeptId())
                .orElseThrow(() -> new RuntimeException("Department not found"));

        Designation designation = designationRepository.findById(request.getDesignationId())
                .orElseThrow(() -> new RuntimeException("Designation not found"));

        EmploymentType empType = employmentTypeRepository.findById(request.getEmploymentTypeId())
                .orElseThrow(() -> new RuntimeException("Employment Type not found"));


        Employee employee = Employee.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .email(request.getEmail())
                .phone(request.getPhone())
                .address(request.getAddress())
                .department(department)
                .designation(designation)
                .panNum(request.getPanNum())
                .aadharNum(request.getAadharNum())
                .passportNum(request.getPassportNum())
                .joinDate(request.getJoinDate())
                .offerLetterNum(request.getOfferLetterNum())
                .releaseDate(request.getReleaseDate())
                .reportingManager(request.getReportingManager())
                .employmentType(empType)
                .noticePeriod(request.getNoticePeriod())
                .isActive(true)
                .createdBy(request.getCreatedBy())
                .createdOn(Instant.now())
                .build();

        Employee savedEmployee = repository.save(employee);

        String username = savedEmployee.getEmail();
        String defaultPassword = generateDefaultPassword();
        String encodedPassword = onboardingPasswordEncoder.encode(defaultPassword);

        User user = User.builder()
                .username(username)
                .password(encodedPassword)
                .role(Role.EMPLOYEE)
                .employee(savedEmployee)
                .isActive(true)
                .createdBy(request.getCreatedBy())
                .createdOn(Instant.now())
                .build();

        userRepository.save(user);

        return EmpResponseDto.builder()
                .message("Employee added successfully")
                .username(username)
                .password(defaultPassword)
                .empId(savedEmployee.getEmpId())
                .build();
    }

    @Override
    public EmpResponseDto updateEmployee(UUID id, EmpRequestDto request) {

        Employee employee = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Employee not found"));

        if (request.getDeptId() != null) {
            Department department = departmentRepository.findById(request.getDeptId())
                    .orElseThrow(() -> new RuntimeException("Department not found"));
            employee.setDepartment(department);
        }

        if (request.getDesignationId() != null) {
            Designation designation = designationRepository.findById(request.getDesignationId())
                    .orElseThrow(() -> new RuntimeException("Designation not found"));
            employee.setDesignation(designation);
        }

        if (request.getFirstName() != null)
            employee.setFirstName(request.getFirstName());

        if (request.getLastName() != null)
            employee.setLastName(request.getLastName());

        if (request.getEmail() != null)
            employee.setEmail(request.getEmail());

        if (request.getPhone() != null)
            employee.setPhone(request.getPhone());

        if (request.getAddress() != null)
            employee.setAddress(request.getAddress());

        if (request.getPanNum() != null)
            employee.setPanNum(request.getPanNum());

        if (request.getAadharNum() != null)
            employee.setAadharNum(request.getAadharNum());

        if (request.getPassportNum() != null)
            employee.setPassportNum(request.getPassportNum());

        if (request.getJoinDate() != null)
            employee.setJoinDate(request.getJoinDate());

        if (request.getOfferLetterNum() != null)
            employee.setOfferLetterNum(request.getOfferLetterNum());

        if (request.getReleaseDate() != null)
            employee.setReleaseDate(request.getReleaseDate());

        if (request.getReportingManager() != null)
            employee.setReportingManager(request.getReportingManager());


        if (request.getEmploymentTypeId() != null) {

            EmploymentType empType = employmentTypeRepository.findById(request.getEmploymentTypeId())
                    .orElseThrow(() -> new RuntimeException("Employment Type not found"));

            employee.setEmploymentType(empType);

        }


        if (request.getNoticePeriod() != null)
            employee.setNoticePeriod(request.getNoticePeriod());

        if (request.getUpdatedBy() != null)
            employee.setUpdatedBy(request.getUpdatedBy());

        employee.setUpdatedOn(Instant.now());

        repository.save(employee);

        return EmpResponseDto.builder()
                .message("Employee updated successfully")
                .build();
    }

    @Override
    public EmpResponseDto deactivateEmployee(UUID id) {
        Employee employee = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Employee not found"));

        employee.setIsActive(false);
        employee.setUpdatedOn(Instant.now());

        repository.save(employee);

        return EmpResponseDto.builder()
                .message("Employee deactivated successfully")
                .build();
    }

    @Override
    public Employee getEmployeeById(UUID id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Employee not found"));
    }

    @Override
    public List<Employee> getAllEmployees() {
        return repository.findByIsActiveTrue();
    }
}
