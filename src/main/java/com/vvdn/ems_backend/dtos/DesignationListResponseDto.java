package com.vvdn.ems_backend.dtos;

import lombok.Builder;
import lombok.Data;

import java.time.Instant;
import java.util.UUID;

@Data
@Builder
public class DesignationListResponseDto {

    private UUID id;
    private String title;
    private String description;
    private Boolean isActive;
    private Instant createdOn;
}
