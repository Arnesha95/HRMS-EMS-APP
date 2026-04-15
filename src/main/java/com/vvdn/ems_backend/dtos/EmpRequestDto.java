package com.vvdn.ems_backend.dtos;

import lombok.Data;

import java.time.LocalDate;
import java.util.UUID;

@Data
public class EmpRequestDto {

    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private String address;

    private UUID deptId;
    private UUID designationId;

    private String panNum;
    private String aadharNum;
    private String passportNum;

    private LocalDate joinDate;
    private String offerLetterNum;
    private LocalDate releaseDate;

    private UUID reportingManager;

    private UUID employmentTypeId;

    private Short noticePeriod;

    private UUID createdBy;
    private UUID updatedBy;
}
