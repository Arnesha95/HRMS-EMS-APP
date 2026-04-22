package com.vvdn.ems_backend.dtos;

import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class DesignationDto {
    private UUID id;
    private String name;
}
