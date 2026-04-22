package com.vvdn.ems_backend.services;

import com.vvdn.ems_backend.dtos.DeptDesigMappingRequestDto;
import com.vvdn.ems_backend.dtos.DeptWiseDesigResponseDto;
import com.vvdn.ems_backend.dtos.DesignationDto;

import java.util.List;
import java.util.UUID;

public interface DeptDesigMappingService {


    DeptWiseDesigResponseDto addMappings(DeptDesigMappingRequestDto dto);

    //DeptDesigMappingResponseDto patchMapping(UUID id, DeptDesigMappingRequestDto dto);
    List<UUID> getDesignationsByDept(UUID deptId);

    DeptWiseDesigResponseDto deleteMapping(UUID deptId, UUID desigId);

   // List<DeptWiseDesigResponseDto> getAllDeptWithDesignations();


    List<DesignationDto> getDesignationDetailsByDept(UUID deptId);
}
