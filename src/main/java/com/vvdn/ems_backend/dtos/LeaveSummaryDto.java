package com.vvdn.ems_backend.dtos;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class LeaveSummaryDto {

    // Request stats
    private long totalRequests;
    private long pendingRequests;
    private long approvedRequests;
    private long rejectedRequests;

    // Leave balance stats
    private float totalAllocated;
    private float totalUsed;
    private float totalRemaining;

    // Breakdown
    private List<LeaveTypeSummaryDto> leaveTypeBreakdown;
}
