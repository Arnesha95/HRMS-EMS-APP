package com.vvdn.ems_backend.dtos;

import lombok.Data;

import java.time.LocalDate;
import java.util.UUID;

@Data
public class ResignationRequestDto {

    //private UUID empId;
   // private LocalDate resignationDate;
    private LocalDate proposedLastWorkingDate;
    private String reason;

}
