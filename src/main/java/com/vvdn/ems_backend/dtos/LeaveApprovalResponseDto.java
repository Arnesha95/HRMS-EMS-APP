package com.vvdn.ems_backend.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
@AllArgsConstructor
public class LeaveApprovalResponseDto {

    private String employeeName;
    private String message;
    private String createdOn;

}
