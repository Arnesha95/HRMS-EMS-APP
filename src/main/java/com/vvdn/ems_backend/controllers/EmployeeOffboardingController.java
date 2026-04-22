package com.vvdn.ems_backend.controllers;

import com.vvdn.ems_backend.dtos.*;
import com.vvdn.ems_backend.entity.OffboardingStatus;
import com.vvdn.ems_backend.services.EmployeeOffboardingService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/offboarding")
@RequiredArgsConstructor
public class EmployeeOffboardingController {

    private final EmployeeOffboardingService service;


    @PostMapping("/resignation")
    public ResponseEntity<ResignationResponseDto> applyResignation(@RequestBody ResignationRequestDto dto) {

        return ResponseEntity.ok(service.applyResignation(dto));

    }


    @GetMapping("/resignation")
    public ResponseEntity<List<ResignationResponseDto>> getResignations(
            @RequestParam(required = false) OffboardingStatus status
    ) {

        List<ResignationResponseDto> list = service.getResignations(status);

        if (list.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(list);
    }


    @PutMapping("/resignation/{offBoardingId}/action")
    public ResponseEntity<ResignationResponseDto> takeAction(
            @PathVariable UUID offBoardingId,
            @Valid @RequestBody HrActionDto dto
    ) {

        UUID hrId = UUID.randomUUID();

        return ResponseEntity.ok(service.takeAction(offBoardingId, dto, hrId));

    }


    @PostMapping("/termination")
    public ResponseEntity<TerminationResponseDto> initiateTermination(
            @RequestBody TerminationRequestDto dto
    ) {

        UUID hrId = UUID.randomUUID();

        return ResponseEntity.ok(service.initiateTermination(dto, hrId));
    }



    @GetMapping("/termination")
    public ResponseEntity<List<TerminationResponseDto>> getTerminations(
            @RequestParam(required = false) OffboardingStatus status
    ) {

        List<TerminationResponseDto> list = service.getTerminations(status);

        if (list.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(list);
    }

    @PutMapping("/termination/{offBoardingId}/action")
    public ResponseEntity<TerminationResponseDto> takeTerminationAction(
            @PathVariable UUID offBoardingId,
            @Valid @RequestBody HrActionDto dto
    ) {
        UUID hrId = UUID.randomUUID();
        return ResponseEntity.ok(service.takeTerminationAction(offBoardingId, dto, hrId));
    }

}
