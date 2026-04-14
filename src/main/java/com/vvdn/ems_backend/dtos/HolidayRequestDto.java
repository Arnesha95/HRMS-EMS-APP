package com.vvdn.ems_backend.dtos;

import lombok.Data;

import java.time.LocalDate;
import java.util.UUID;

@Data
public class HolidayRequestDto {

    private String holidayName;
    private LocalDate holidayDate;
    private String holidayType;
    private Short calendarYear;
    private Boolean isActive;
    private UUID createdBy;
    private UUID updatedBy;
}
