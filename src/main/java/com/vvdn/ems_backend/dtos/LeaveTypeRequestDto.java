package com.vvdn.ems_backend.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LeaveTypeRequestDto {

    private String type;

    private Boolean carryForwardAllowed;

    private Boolean postApplicationAllowed;

    private Short maxConsecutiveDays;

    private UUID createdBy;

    private UUID updatedBy;

}
