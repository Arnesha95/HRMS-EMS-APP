package com.vvdn.ems_backend.dtos;

import lombok.Data;

import java.time.LocalDate;
import java.util.UUID;

@Data
public class TerminationRequestDto {

    private UUID empId;
    private LocalDate terminationDate;
    private String reason;
    private String feedback;
    private Boolean isGoodToRehire;

}
