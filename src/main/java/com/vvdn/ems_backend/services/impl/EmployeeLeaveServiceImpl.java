package com.vvdn.ems_backend.services.impl;

import com.vvdn.ems_backend.dtos.EmployeeLeaveResponseDto;
import com.vvdn.ems_backend.entity.Employee;
import com.vvdn.ems_backend.entity.EmployeeLeave;
import com.vvdn.ems_backend.entity.LeavePolicy;
import com.vvdn.ems_backend.entity.LeaveType;
import com.vvdn.ems_backend.exception.BadRequestException;
import com.vvdn.ems_backend.exception.LeaveAlreadyAllocatedException;
import com.vvdn.ems_backend.repository.EmpRepository;
import com.vvdn.ems_backend.repository.EmployeeLeaveRepository;
import com.vvdn.ems_backend.repository.LeavePolicyRepository;
import com.vvdn.ems_backend.repository.LeaveTypeRepository;
import com.vvdn.ems_backend.services.EmployeeLeaveService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class EmployeeLeaveServiceImpl implements EmployeeLeaveService {

    private final EmpRepository employeeRepository;
    private final LeavePolicyRepository leavePolicyRepository;
    private final EmployeeLeaveRepository employeeLeaveRepository;
    private final LeaveTypeRepository leaveTypeRepository;

    @Override
    public List<EmployeeLeaveResponseDto> allocateLeavesToEmployee(UUID empId) {

        Employee employee = employeeRepository.findById(empId)
                .orElseThrow(() -> new RuntimeException("Employee not found"));

        if (!employeeLeaveRepository.findByEmployee_EmpId(empId).isEmpty()) {
            throw new LeaveAlreadyAllocatedException("Leaves already allocated for this employee");
        }


        int currentYear = LocalDate.now().getYear();


        List<LeavePolicy> policies = leavePolicyRepository
                .findByYear((int) currentYear);

        if (policies.isEmpty()) {
            throw new RuntimeException("No leave policy found");
        }

        List<EmployeeLeave> employeeLeavesList = new ArrayList<>();
        List<EmployeeLeaveResponseDto> responseList = new ArrayList<>();


        LocalDate joiningDate = employee.getJoinDate();
        int joiningMonth = joiningDate.getMonthValue();
        int remainingMonths = 12 - joiningMonth + 1;

        for (LeavePolicy policy : policies) {

            float totalAnnualLeaves = policy.getNoOfDays() != null
                    ? policy.getNoOfDays()
                    : 0;

            float proratedLeaves = (joiningMonth == 1)
                    ? totalAnnualLeaves
                    : (totalAnnualLeaves / 12) * remainingMonths;

            proratedLeaves = Math.round(proratedLeaves * 100) / 100f;

            String leaveTypeName = policy.getLeaveType().getType();

            EmployeeLeave empLeave = EmployeeLeave.builder()
                    .employee(employee)
                    .leavePolicy(policy)
                    .totalLeaves(proratedLeaves)
                    .usedLeaves(0f)
                    .remainingLeaves(proratedLeaves)
                    .createdOn(Instant.now())
                    .build();

            employeeLeavesList.add(empLeave);

            responseList.add(
                    EmployeeLeaveResponseDto.builder()
                            .leaveType(leaveTypeName)
                            .totalLeaves(proratedLeaves)
                            .usedLeaves(0f)
                            .remainingLeaves(proratedLeaves)
                            .build()
            );
        }

        employeeLeaveRepository.saveAll(employeeLeavesList);

        return responseList;
    }

}
