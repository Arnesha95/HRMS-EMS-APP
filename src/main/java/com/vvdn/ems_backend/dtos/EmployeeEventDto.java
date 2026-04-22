package com.vvdn.ems_backend.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
public class EmployeeEventDto {
    private UUID empId;
    private String name;
    private LocalDate date;
}
