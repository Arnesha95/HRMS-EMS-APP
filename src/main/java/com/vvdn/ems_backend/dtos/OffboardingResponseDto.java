package com.vvdn.ems_backend.dtos;

import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class OffboardingResponseDto {

    private String message;
    private UUID empId;
    private String status;
}
