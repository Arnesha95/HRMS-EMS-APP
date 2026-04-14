package com.vvdn.ems_backend.dtos;

import lombok.Builder;
import lombok.Data;

import java.time.Instant;
import java.util.UUID;

@Data
@Builder
public class DepartmentListResponseDto {

    private UUID id;
    private String deptName;
    private String description;
    private Boolean isActive;
    private Instant createdOn;
    private Instant updatedOn;
}
