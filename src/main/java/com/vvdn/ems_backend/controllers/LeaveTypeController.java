package com.vvdn.ems_backend.controllers;

import com.vvdn.ems_backend.dtos.*;

import com.vvdn.ems_backend.entity.LeaveType;
import com.vvdn.ems_backend.services.LeaveTypeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class LeaveTypeController {

    private final LeaveTypeService leaveTypeService;

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/leaveType")
    public ResponseEntity<LeaveTypeResponseDto> createLeaveType(@RequestBody LeaveTypeRequestDto leaveTypeRequestDto){

        LeaveTypeResponseDto response = leaveTypeService.createLeaveType(leaveTypeRequestDto);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PreAuthorize("hasRole('ADMIN', 'HR')")
    @GetMapping("/leaveType/{leaveTypeId}")
    public ResponseEntity<LeaveType> getLeaveType(
            @PathVariable UUID leaveTypeId) {

        return ResponseEntity.ok(
                leaveTypeService.getLeaveType(leaveTypeId)
        );
    }

    @GetMapping("/leaveTypes")
    public ResponseEntity<List<LeaveType>>getAllLeaveTypes(){

        return ResponseEntity.ok(
                leaveTypeService.getAllLeaveTypes()
        );
    }

    @PatchMapping("/leaveType/{leaveTypeId}")
    public ResponseEntity<LeaveTypeResponseDto> updateLeaveType(
            @PathVariable UUID leaveTypeId,
            @RequestBody LeaveTypeRequestDto leaveTypeRequestDto) {

        LeaveTypeResponseDto response =
                leaveTypeService.updateLeaveType(leaveTypeId, leaveTypeRequestDto);

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }


    @DeleteMapping("/leaveType/{leaveTypeId}")
    public ResponseEntity<LeaveTypeResponseDto> deleteLeaveType(@PathVariable UUID leaveTypeId) {

        LeaveTypeResponseDto response = leaveTypeService.deleteLeaveType(leaveTypeId);

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

}
