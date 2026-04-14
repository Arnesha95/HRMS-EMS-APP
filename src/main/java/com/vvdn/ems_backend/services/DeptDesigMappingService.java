package com.vvdn.ems_backend.services;

import com.vvdn.ems_backend.dtos.DeptDesigMappingRequestDto;
import com.vvdn.ems_backend.dtos.DeptDesigMappingResponseDto;

import java.util.List;
import java.util.UUID;

public interface DeptDesigMappingService {


    DeptDesigMappingResponseDto addMappings(DeptDesigMappingRequestDto dto);

    //DeptDesigMappingResponseDto patchMapping(UUID id, DeptDesigMappingRequestDto dto);
    List<UUID> getDesignationsByDept(UUID deptId);

    DeptDesigMappingResponseDto deleteMapping(UUID deptId, UUID desigId);
}
