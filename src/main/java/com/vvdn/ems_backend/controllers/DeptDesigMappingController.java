package com.vvdn.ems_backend.controllers;

import com.vvdn.ems_backend.dtos.DeptDesigMappingRequestDto;
import com.vvdn.ems_backend.dtos.DeptWiseDesigResponseDto;
import com.vvdn.ems_backend.dtos.DesignationDto;
import com.vvdn.ems_backend.services.DeptDesigMappingService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class DeptDesigMappingController {

    private final DeptDesigMappingService service;


    @PostMapping("/dept-desig")
    public DeptWiseDesigResponseDto addMappings(
            @RequestBody DeptDesigMappingRequestDto requestDto
    ) {
        return service.addMappings(requestDto);
    }


    @GetMapping("/dept-desig/{deptId}/details")
    public List<DesignationDto> getDesignationDetailsByDept(
            @PathVariable UUID deptId
    ) {
        return service.getDesignationDetailsByDept(deptId);
    }


    @GetMapping("/dept-desig/{deptId}")
    public List<UUID> getDesignationsByDept(
            @PathVariable UUID deptId
    ) {
        return service.getDesignationsByDept(deptId);
    }

    @DeleteMapping("/dept-desig")
    public DeptWiseDesigResponseDto deleteMapping(
            @RequestParam UUID deptId,
            @RequestParam UUID desigId
    ) {
        return service.deleteMapping(deptId, desigId);
    }


}
