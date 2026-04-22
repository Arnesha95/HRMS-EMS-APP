package com.vvdn.ems_backend.controllers;

import com.vvdn.ems_backend.dtos.DepartmentListResponseDto;
import com.vvdn.ems_backend.dtos.DepartmentRequestDto;
import com.vvdn.ems_backend.dtos.DepartmentResponseDto;
import com.vvdn.ems_backend.services.impl.DepartmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/departments")
@RequiredArgsConstructor
public class DepartmentController {

    private final DepartmentService departmentService;


    @PostMapping
    public ResponseEntity<DepartmentResponseDto>addDepartment(@RequestBody DepartmentRequestDto departmentRequestDto){

        DepartmentResponseDto response = departmentService.addDepartment(departmentRequestDto);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public ResponseEntity<List<DepartmentListResponseDto>>getActiveDepartments(){
        return ResponseEntity.ok(departmentService.getActiveDepartments());
    }


    @GetMapping("/{deptId}")
    public ResponseEntity<DepartmentListResponseDto>getDepartmentById(@PathVariable UUID id){
        return ResponseEntity.ok(departmentService.getDepartmentByID(id));
    }



    @PutMapping("/{deptId}")
    public ResponseEntity<DepartmentResponseDto> updateDepartment(
            @PathVariable UUID id,
            @RequestBody DepartmentRequestDto request) {

        DepartmentResponseDto response = departmentService.updateDepartment(id, request);

        return ResponseEntity.ok(response);
    }


    @DeleteMapping("/{deptId}")
    public ResponseEntity<DepartmentResponseDto> deleteDepartment(@PathVariable UUID id) {

        DepartmentResponseDto response = departmentService.deleteDepartment(id);

        return ResponseEntity.ok(response);
    }


    @PatchMapping("/{deptId}/deactivate")
    public ResponseEntity<DepartmentResponseDto> deactivateDepartment(@PathVariable UUID deptId) {

        DepartmentResponseDto response = departmentService.deactivateDepartment(deptId);
        return ResponseEntity.ok(response);
    }
}
