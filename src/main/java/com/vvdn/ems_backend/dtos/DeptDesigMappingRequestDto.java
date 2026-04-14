package com.vvdn.ems_backend.dtos;

import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
public class DeptDesigMappingRequestDto {

    private UUID deptId;
    private List<UUID> desigIds; // used for ADD
//    private UUID desigId;        // used for PATCH
    private UUID userId;

}
