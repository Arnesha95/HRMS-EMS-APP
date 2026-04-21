package com.vvdn.ems_backend.dtos;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class EmployeeSummaryDto {

    private long totalEmployees;
    private long activeEmployees;
    private long inactiveEmployees;
    private long totalOnboardings;
    private long todayOnboardings;
    private double employeeGrowthPercentage;
}
