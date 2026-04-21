package com.vvdn.ems_backend.dtos;

import com.vvdn.ems_backend.entity.OffboardingStatus;
import lombok.Data;

import java.time.LocalDate;


@Data
public class HrResignationActionDto {

    private OffboardingStatus status;
    private LocalDate finalLastWorkingDate;
    private String feedback;
    private Boolean isGoodToRehire;

}
