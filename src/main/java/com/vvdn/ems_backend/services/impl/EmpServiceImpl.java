package com.vvdn.ems_backend.services.impl;

import com.vvdn.ems_backend.dtos.EmpRequestDto;
import com.vvdn.ems_backend.dtos.EmpResponseDto;
import com.vvdn.ems_backend.dtos.EmployeeEventDto;
import com.vvdn.ems_backend.dtos.EmployeeSummaryDto;
import com.vvdn.ems_backend.entity.*;
import com.vvdn.ems_backend.repository.*;
import com.vvdn.ems_backend.services.EmpService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
                .dateOfBirth(request.getDateOfBirth())
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
                .build();


        Employee savedEmployee = repository.save(employee);


        String username = savedEmployee.getEmail();
        String defaultPassword = generateDefaultPassword();
        String encodedPassword = onboardingPasswordEncoder.encode(defaultPassword);


        Role assignedRole;
        if (request.getRole() != null) {
            assignedRole = request.getRole();
        } else {
            assignedRole = Role.EMPLOYEE;
        }


        User user = User.builder()
                .username(username)
                .password(encodedPassword)
                .role(assignedRole)
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
    public EmpResponseDto updateEmployee(UUID empId, EmpRequestDto request) {

        Employee employee = repository.findById(empId)
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

        if (request.getDateOfBirth() != null)
            employee.setDateOfBirth(request.getDateOfBirth());

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
    public EmpResponseDto deactivateEmployee(UUID empId) {

        Employee employee = repository.findById(empId)
                .orElseThrow(() -> new RuntimeException("Employee not found"));


        employee.setIsActive(false);
        employee.setUpdatedOn(Instant.now());
        repository.save(employee);


        userRepository.findByEmployee(employee).ifPresent(user -> {
            user.setActive(false);
            user.setUpdatedOn(Instant.now());
            userRepository.save(user);
        });

        return EmpResponseDto.builder()
                .message("Employee deactivated successfully")
                .build();
    }

    @Override
    public Employee getEmployeeById(UUID empId) {
        return repository.findById(empId)
                .orElseThrow(() -> new RuntimeException("Employee not found"));
    }

    @Override
    public List<Employee> getAllEmployees() {
        return repository.findByIsActiveTrue();

    }


    @Override
    public EmployeeSummaryDto getEmployeeSummary() {

        long total = repository.count();
        long active = repository.countByIsActiveTrue();
        long inactive = repository.countByIsActiveFalse();

        long totalOnboardings = total;


        ZoneId zone = ZoneId.systemDefault();
        LocalDate today = LocalDate.now(zone);


        Instant startOfDay = today.atStartOfDay(zone).toInstant();
        Instant endOfDay = today.plusDays(1).atStartOfDay(zone).toInstant();

        long todayOnboardings =
                repository.countByCreatedOnBetween(startOfDay, endOfDay);


        LocalDate firstDayCurrentMonth = today.withDayOfMonth(1);
        LocalDate firstDayPreviousMonth = firstDayCurrentMonth.minusMonths(1);

        Instant startCurrentMonth = firstDayCurrentMonth.atStartOfDay(zone).toInstant();


        long previousMonthTotal =
                repository.countByCreatedOnBefore(startCurrentMonth);


        long currentMonthTotal = total;


        double growthPercentage = 0;

        if (previousMonthTotal > 0) {
            growthPercentage =
                    ((double) (currentMonthTotal - previousMonthTotal)
                            / previousMonthTotal) * 100;
        }

        return EmployeeSummaryDto.builder()
                .totalEmployees(total)
                .activeEmployees(active)
                .inactiveEmployees(inactive)
                .totalOnboardings(totalOnboardings)
                .todayOnboardings(todayOnboardings)
                .employeeGrowthPercentage(growthPercentage)
                .build();
    }


    @Override
    public List<EmployeeEventDto> getTodaysBirthdays() {

        LocalDate today = LocalDate.now();

        return repository.findByIsActiveTrue().stream()
                .filter(e -> e.getDateOfBirth() != null &&
                        e.getDateOfBirth().getMonthValue() == today.getMonthValue() &&
                        e.getDateOfBirth().getDayOfMonth() == today.getDayOfMonth())
                .map(e -> EmployeeEventDto.builder()
                        .empId(e.getEmpId())
                        .name(getFullName(e))
                        .date(e.getDateOfBirth())
                        .build())
                .toList();
    }


    @Override
    public List<EmployeeEventDto> getTodaysAnniversaries() {

        LocalDate today = LocalDate.now();

        return repository.findByIsActiveTrue().stream()
                .filter(e -> e.getJoinDate() != null &&
                        e.getJoinDate().getMonthValue() == today.getMonthValue() &&
                        e.getJoinDate().getDayOfMonth() == today.getDayOfMonth())
                .map(e -> EmployeeEventDto.builder()
                        .empId(e.getEmpId())
                        .name(getFullName(e))
                        .date(e.getJoinDate())
                        .build())
                .toList();
    }


    private String getFullName(Employee e) {
        return Stream.of(e.getFirstName(), e.getLastName())
                .filter(Objects::nonNull)
                .collect(Collectors.joining(" "));
    }
}
