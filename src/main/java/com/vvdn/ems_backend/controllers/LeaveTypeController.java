package com.vvdn.ems_backend.controllers;

import com.vvdn.ems_backend.dtos.*;
import com.vvdn.ems_backend.entity.AttendancePolicy;
import com.vvdn.ems_backend.entity.LeaveType;
import com.vvdn.ems_backend.services.LeaveTypeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class LeaveTypeController {

    private final LeaveTypeService leaveTypeService;

    @PostMapping("/leaveType")
    public ResponseEntity<LeaveTypeResponseDto> createLeaveType(@RequestBody LeaveTypeRequestDto leaveTypeRequestDto){

        LeaveTypeResponseDto response = leaveTypeService.createLeaveType(leaveTypeRequestDto);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/leaveType/{id}")
    public ResponseEntity<LeaveType> getLeaveType(
            @PathVariable UUID attPolicyId) {

        return ResponseEntity.ok(
                leaveTypeService.getLeaveType(attPolicyId)
        );
    }

    @GetMapping("/leaveTypes")
    public ResponseEntity<List<LeaveType>>getAllLeaveTypes(){

        return ResponseEntity.ok(
                leaveTypeService.getAllLeaveTypes()
        );
    }

    @PatchMapping("/leaveType/{id}")
    public ResponseEntity<LeaveTypeResponseDto> updateLeaveType(
            @PathVariable UUID id,
            @RequestBody LeaveTypeRequestDto leaveTypeRequestDto) {

        LeaveTypeResponseDto response =
                leaveTypeService.updateLeaveType(id, leaveTypeRequestDto);

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }


    @DeleteMapping("/leaveType/{id}")
    public ResponseEntity<LeaveTypeResponseDto> deleteLeaveType(@PathVariable UUID id) {

        LeaveTypeResponseDto response = leaveTypeService.deleteLeaveType(id);

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

}
