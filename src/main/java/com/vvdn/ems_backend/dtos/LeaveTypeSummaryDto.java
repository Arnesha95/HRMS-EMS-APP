package com.vvdn.ems_backend.dtos;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LeaveTypeSummaryDto {

    private String leaveType;

    private float allocated;
    private float used;
    private float remaining;

    private long pending;
    private long approved;
    private long rejected;
}
