package com.vvdn.ems_backend.controllers;

import com.vvdn.ems_backend.dtos.*;
import com.vvdn.ems_backend.services.impl.DesignationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class DesignationController {

    private final DesignationService designationService;

    @PostMapping("/designation")
    public ResponseEntity<DesignationResponseDto> addDesignation(@RequestBody DesignationRequestDto designationRequestDto){

        DesignationResponseDto response = designationService.addDesignation(designationRequestDto);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/designations")
    public ResponseEntity<List<DesignationListResponseDto>>getActiveDesignations(){
        return ResponseEntity.ok(designationService.getActiveDesignations());
    }

    @GetMapping("/designation/{id}")
    public ResponseEntity<DesignationListResponseDto>getDepartmentById(@PathVariable UUID id){
        return ResponseEntity.ok(designationService.getDesignationByID(id));
    }

    @PutMapping("/designation/{id}")
    public ResponseEntity<DesignationResponseDto> updateDesignation(
            @PathVariable UUID id,
            @RequestBody DesignationRequestDto request) {

        DesignationResponseDto response = designationService.updateDesignation(id, request);

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/designation/{id}")
    public ResponseEntity<DesignationResponseDto> deleteDesignation(@PathVariable UUID id) {

        DesignationResponseDto response = designationService.deleteDesignation(id);

        return ResponseEntity.ok(response);
    }

    @PatchMapping("/designation/{id}/deactivate")
    public ResponseEntity<DesignationResponseDto> deactivateDesignation(@PathVariable UUID id) {

        DesignationResponseDto response = designationService.deactivateDesignation(id);
        return ResponseEntity.ok(response);
    }

}
