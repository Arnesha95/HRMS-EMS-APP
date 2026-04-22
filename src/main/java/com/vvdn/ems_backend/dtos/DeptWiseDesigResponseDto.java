package com.vvdn.ems_backend.dtos;

import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.UUID;


@Data
@Builder
public class DeptWiseDesigResponseDto {

    private UUID deptId;
    private List<UUID> desigIds;

    private String message;
}
