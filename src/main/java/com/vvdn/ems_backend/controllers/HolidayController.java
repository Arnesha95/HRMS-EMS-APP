package com.vvdn.ems_backend.controllers;

import com.vvdn.ems_backend.dtos.HolidayRequestDto;
import com.vvdn.ems_backend.dtos.HolidayResponseDto;
import com.vvdn.ems_backend.entity.HolidayCalendar;
import com.vvdn.ems_backend.services.HolidayService;
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
public class HolidayController {

    private final HolidayService holidayService;

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/holiday")
    public ResponseEntity<HolidayResponseDto> createPolicy(
            @RequestBody HolidayRequestDto request) {

        return new ResponseEntity<>(
                holidayService.addHoliday(request),
                HttpStatus.CREATED
        );
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'HR', 'EMPLOYEE)")
    @GetMapping("/holiday/{holidayId}")
    public ResponseEntity<HolidayCalendar> getHolidayById(
            @PathVariable UUID holidayId) {

        return ResponseEntity.ok(
                holidayService.getHolidayById(holidayId)
        );
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'HR', 'EMPLOYEE')")
    @GetMapping("/holidays")
    public ResponseEntity<List<HolidayCalendar>> getAllHolidays() {
        return ResponseEntity.ok(
                holidayService.getAllHolidays()
        );
    }

    @PreAuthorize("hasAnyRole('ADMIN')")
    @PutMapping("/holiday/{holidayId}")
    public ResponseEntity<HolidayResponseDto> updateHoliday(
            @PathVariable UUID holidayId,
            @RequestBody HolidayRequestDto request) {

        return ResponseEntity.ok(
                holidayService.updateHoliday(holidayId, request)
        );
    }

    @PreAuthorize("hasAnyRole('ADMIN')")
    @DeleteMapping("/holiday/{holidayId}/deactivate")
    public ResponseEntity<HolidayResponseDto> deactivateHoliday(
            @PathVariable UUID holidayId) {

        return ResponseEntity.ok(
                holidayService.deactivateHoliday(holidayId)
        );
    }

}
