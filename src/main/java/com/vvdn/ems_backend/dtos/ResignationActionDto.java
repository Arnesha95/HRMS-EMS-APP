package com.vvdn.ems_backend.dtos;

import lombok.Data;

import java.time.LocalDate;
import java.util.UUID;

@Data
public class ResignationActionDto {

    private UUID resignationId;
    private String action; // APPROVED / REJECTED
    private LocalDate finalLastWorkingDay;
}
