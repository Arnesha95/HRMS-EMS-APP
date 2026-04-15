package com.vvdn.ems_backend.dtos;

import lombok.Data;

import java.time.LocalDate;
import java.util.UUID;

@Data
public class OffboardingRequestDto {

    private UUID empId;

    private LocalDate lastWorkingDay;
    private String offboardingType;

    private String exitReason;

    private Boolean clearanceStatus;
    private Boolean rehireEligible;

    private String noticePeriodStatus;
}
