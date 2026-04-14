package com.vvdn.ems_backend.dtos;

import lombok.Data;

import java.util.UUID;

@Data
public class EmploymentTypeRequestDto {

    private String name;
    private Boolean isActive;

    private UUID createdBy;
    private UUID updatedBy;
}
