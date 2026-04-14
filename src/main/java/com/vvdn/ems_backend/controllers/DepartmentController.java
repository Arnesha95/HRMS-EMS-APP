package com.vvdn.ems_backend.controllers;

import com.vvdn.ems_backend.dtos.DepartmentListResponseDto;
import com.vvdn.ems_backend.dtos.DepartmentRequestDto;
import com.vvdn.ems_backend.dtos.DepartmentResponseDto;
import com.vvdn.ems_backend.services.impl.DepartmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class DepartmentController {

    private final DepartmentService departmentService;

    @PostMapping("/department")
    public ResponseEntity<DepartmentResponseDto>addDepartment(@RequestBody DepartmentRequestDto departmentRequestDto){

        DepartmentResponseDto response = departmentService.addDepartment(departmentRequestDto);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/departments")
    public ResponseEntity<List<DepartmentListResponseDto>>getActiveDepartments(){
        return ResponseEntity.ok(departmentService.getActiveDepartments());
    }

    @GetMapping("/department/{id}")
    public ResponseEntity<DepartmentListResponseDto>getDepartmentById(@PathVariable UUID id){
        return ResponseEntity.ok(departmentService.getDepartmentByID(id));
    }

    @PutMapping("/department/{id}")
    public ResponseEntity<DepartmentResponseDto> updateDepartment(
            @PathVariable UUID id,
            @RequestBody DepartmentRequestDto request) {

        DepartmentResponseDto response = departmentService.updateDepartment(id, request);

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/department/{id}")
    public ResponseEntity<DepartmentResponseDto> deleteDepartment(@PathVariable UUID id) {

        DepartmentResponseDto response = departmentService.deleteDepartment(id);

        return ResponseEntity.ok(response);
    }

    @PatchMapping("/department/{id}/deactivate")
    public ResponseEntity<DepartmentResponseDto> deactivateDepartment(@PathVariable UUID id) {

        DepartmentResponseDto response = departmentService.deactivateDepartment(id);
        return ResponseEntity.ok(response);
    }
}
