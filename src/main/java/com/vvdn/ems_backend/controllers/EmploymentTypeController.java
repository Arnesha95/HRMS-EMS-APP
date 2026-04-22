package com.vvdn.ems_backend.controllers;

import com.vvdn.ems_backend.dtos.EmploymentTypeRequestDto;
import com.vvdn.ems_backend.dtos.EmploymentTypeResponseDto;
import com.vvdn.ems_backend.entity.EmploymentType;
import com.vvdn.ems_backend.services.EmploymentTypeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class EmploymentTypeController {

    private final EmploymentTypeService service;

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/employment-type")
    public ResponseEntity<EmploymentTypeResponseDto> createEmploymentType(
            @RequestBody EmploymentTypeRequestDto request) {

        return ResponseEntity.ok(service.createEmploymentType(request));
    }


    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/employment-type/{id}")
    public ResponseEntity<EmploymentTypeResponseDto> updateEmploymentType(
            @PathVariable UUID id,
            @RequestBody EmploymentTypeRequestDto request) {

        return ResponseEntity.ok(service.updateEmploymentType(id, request));
    }


    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/employment-type/{id}")
    public ResponseEntity<EmploymentTypeResponseDto> patchEmploymentType(
            @PathVariable UUID id,
            @RequestBody EmploymentTypeRequestDto request) {

        return ResponseEntity.ok(service.patchEmploymentType(id, request));
    }

    @DeleteMapping("/employment-type/{id}")
    public ResponseEntity<EmploymentTypeResponseDto> deleteEmploymentType(
            @PathVariable UUID id) {

        return ResponseEntity.ok(service.deleteEmploymentType(id));
    }


    @PreAuthorize("hasAnyRole('ADMIN', 'HR', 'EMPLOYEE')")
    @GetMapping("/employment-types")
    public ResponseEntity<List<EmploymentType>> getAllEmploymentTypes() {
        return ResponseEntity.ok(service.getAllEmploymentTypes());
    }


    @PreAuthorize("hasAnyRole('ADMIN', 'HR', 'EMPLOYEE')")
    @GetMapping("/employement-type/{id}")
    public ResponseEntity<EmploymentType> getEmploymentTypeById(@PathVariable UUID id) {
        return ResponseEntity.ok(service.getEmploymentTypeById(id));
    }
}
