package com.vvdn.ems_backend.dtos;

import lombok.Data;

import java.util.UUID;

@Data
public class DesignationRequestDto {

    private String title;
    private String description;
    private Boolean isActive;
    private UUID createdBy;
    private UUID updatedBy;
}
